package com.endre.demo.domain;

import java.time.Instant;
import java.util.UUID;

public class Vote {
    private UUID id;
    private Instant publishedAt;
    private User voter;
    private VoteOption option;

    public Vote() { }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Instant getPublishedAt() { return publishedAt; }
    public void setPublishedAt(Instant publishedAt) { this.publishedAt = publishedAt; }

    public User getVoter() { return voter; }
    public void setVoter(User voter) { this.voter = voter; }

    public VoteOption getOption() { return option; }
    public void setOption(VoteOption option) { this.option = option; }
}
