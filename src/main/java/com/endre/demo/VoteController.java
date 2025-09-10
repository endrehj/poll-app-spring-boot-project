package com.endre.demo;

import com.endre.demo.domain.PollManager;
import com.endre.demo.domain.User;
import com.endre.demo.domain.Vote;
import com.endre.demo.domain.VoteOption;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Collection;
import java.util.UUID;

@RestController
@RequestMapping("/votes")
public class VoteController  {
    private final PollManager pm;
    public VoteController(PollManager pm) {
        this.pm = pm;
    }

    //dto
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

        return pm.saveOrReplaceVote(voter, option, Instant.now());
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
        pm.deleteVote(id);
    }
}
