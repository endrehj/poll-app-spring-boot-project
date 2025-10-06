package com.endre.demo;

import com.endre.demo.domain.PollManager;
import com.endre.demo.domain.User;
import com.endre.demo.domain.Vote;
import com.endre.demo.domain.VoteOption;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Collection;
import java.util.UUID;

@CrossOrigin(
        origins = "http://localhost:5173",
        allowedHeaders = "*",
        methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.OPTIONS }
)
@RestController
@RequestMapping("/votes")
public class VoteController {
    private final PollManager pm;
    private final VoteCountCache cache;

    public VoteController(PollManager pm, VoteCountCache cache) {
        this.pm = pm;
        this.cache = cache;
    }

    public static class CreateVoteDto {
        public UUID voterId;
        public UUID optionId;
    }

    @PostMapping
    public Vote castVote(@RequestBody CreateVoteDto dto) {
        User voter = pm.findUser(dto.voterId)
                .orElseThrow(() -> new RuntimeException("Voter not found"));
        VoteOption option = pm.findOption(dto.optionId)
                .orElseThrow(() -> new RuntimeException("Option not found"));

        Vote v = pm.saveOrReplaceVote(voter, option, Instant.now());

        cache.evict(option.getPoll().getId());
        return v;
    }

    @GetMapping
    public Collection<Vote> listVotes() {
        return pm.findAllVotes();
    }

    @GetMapping("/{id}")
    public Vote getVote(@PathVariable UUID id) {
        return pm.findVote(id).orElseThrow(() -> new RuntimeException("Vote not found"));
    }

    @DeleteMapping("/{id}")
    public void deleteVote(@PathVariable UUID id) {
        pm.findVote(id).ifPresent(v -> {
            if (v.getOption() != null && v.getOption().getPoll() != null) {
                cache.evict(v.getOption().getPoll().getId());
            }
        });
        pm.deleteVote(id);
    }
}
