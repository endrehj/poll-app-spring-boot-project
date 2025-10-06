package com.endre.demo.domain;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

@Component
public class PollManager {
    private final Map<UUID, User> users = new HashMap<>();
    private final Map<UUID, Poll> polls = new HashMap<>();
    private final Map<UUID, Vote> votes = new HashMap<>();
    private final Map<UUID, VoteOption> options = new HashMap<>();

    public PollManager() { }

    // user
    public User saveUser(User user) {
        if (user.getId() == null) {
            user.setId(UUID.randomUUID());
        }
        users.put(user.getId(), user);
        return user;
    }
    public Optional<User> findUser(UUID id) {
        return Optional.ofNullable(users.get(id));
    }
    public Collection<User> findAllUsers() {
        return users.values();
    }
    public void deleteUser(UUID id) {
        users.remove(id);
    }

    // poll
    public Poll savePoll(Poll poll) {
        if (poll.getId() == null) {
            poll.setId(UUID.randomUUID());
        }

        // assign id to options
        if (poll.getOptions() != null) {
            for (VoteOption o : poll.getOptions()) {
                if (o.getId() == null) {
                    o.setId(UUID.randomUUID());
                }
                o.setPoll(poll);
                options.put(o.getId(), o);
            }
        }

        polls.put(poll.getId(), poll);
        return poll;
    }

    public Optional<Poll> findPoll(UUID id) {
        return Optional.ofNullable(polls.get(id));
    }

    public Collection<Poll> findAllPolls() {
        return polls.values();
    }

    public void deletePoll(UUID id) {
        Poll poll = polls.remove(id);
        if (poll != null && poll.getOptions() != null) {
            // remove all options
            for (VoteOption o : poll.getOptions()) {
                options.remove(o.getId());
                // remove any votes that referenced this option
                votes.values().removeIf(v -> v.getOption() != null &&
                        v.getOption().getId().equals(o.getId()));
            }
        }
    }

    // vote
    public Vote saveOrReplaceVote(User voter, VoteOption option, Instant when) {
        UUID pollId = option.getPoll().getId();

        // check if voter already has a vote for this poll
        Optional<Vote> existingVote = votes.values().stream()
                .filter(v -> v.getVoter().getId().equals(voter.getId()) &&
                        v.getOption().getPoll().getId().equals(pollId))
                .findFirst();

        Vote vote;
        if (existingVote.isPresent()) {
            // update existing vote
            vote = existingVote.get();
            vote.setOption(option);
            vote.setPublishedAt(when);
        } else {
            // create new vote
            vote = new Vote();
            vote.setId(UUID.randomUUID());
            vote.setVoter(voter);
            vote.setOption(option);
            vote.setPublishedAt(when);
            votes.put(vote.getId(), vote);
        }

        return vote;
    }
    public Optional<Vote> findVote(UUID id) {
        return Optional.ofNullable(votes.get(id));
    }
    public Collection<Vote> findAllVotes() {
        return votes.values();
    }
    public void deleteVote(UUID id) {
        votes.remove(id);
    }

    // option
    public Optional<VoteOption> findOption(UUID id) {
        return Optional.ofNullable(options.get(id));
    }
    public Collection<VoteOption> findAllOptions() {
        return options.values();
    }

    public Map<String, Long> computeVoteCounts(UUID pollId) {
        return findAllVotes().stream()
                .filter(v -> v.getOption() != null
                        && v.getOption().getPoll() != null
                        && pollId.equals(v.getOption().getPoll().getId()))
                .collect(Collectors.groupingBy(
                        v -> String.valueOf(v.getOption().getPresentationOrder()),
                        Collectors.counting()
                ));
    }

}
