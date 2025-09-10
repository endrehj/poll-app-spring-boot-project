package com.endre.demo.domain;

import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonBackReference;

public class VoteOption {
    private UUID id;
    private String caption;
    private int presentationOrder;

    @JsonBackReference
    private Poll poll;

    public VoteOption() { }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getCaption() { return caption; }
    public void setCaption(String caption) { this.caption = caption; }

    public int getPresentationOrder() { return presentationOrder; }
    public void setPresentationOrder(int presentationOrder) { this.presentationOrder = presentationOrder; }

    public Poll getPoll() { return poll; }
    public void setPoll(Poll poll) { this.poll = poll; }


}
