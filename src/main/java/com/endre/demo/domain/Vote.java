package com.endre.demo.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "votes")
public class Vote {

    @Id
    @UuidGenerator
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    private Instant publishedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voter_id")
    private User voter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id")
    private VoteOption option;

    public Vote() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Instant getPublishedAt() { return publishedAt; }
    public void setPublishedAt(Instant publishedAt) { this.publishedAt = publishedAt; }

    public User getVoter() { return voter; }
    public void setVoter(User voter) { this.voter = voter; }

    public VoteOption getOption() { return option; }
    public void setOption(VoteOption option) { this.option = option; }
}
