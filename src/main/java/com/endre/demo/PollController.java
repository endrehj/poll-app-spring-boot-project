package com.endre.demo;


import com.endre.demo.domain.Poll;
import com.endre.demo.domain.PollManager;
import com.endre.demo.domain.User;
import com.endre.demo.domain.VoteOption;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.*;

@CrossOrigin(
        origins = "http://localhost:5173",
        allowedHeaders = "*",
        methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.OPTIONS }
)
@RestController
@RequestMapping("/polls")
public class PollController {
    private final PollManager pm;
    private final VoteCountCache cache;

    public PollController(PollManager pm, VoteCountCache cache) {
        this.pm = pm;
        this.cache = cache;
    }

    // dto
    public static class CreatePollDto {
        public String question;
        public UUID creatorId;
        public Instant publishedAt;
        public Instant validUntil;
        public List<OptionIn> options;

        public static class OptionIn {
            public String caption;
            public int presentationOrder;
        }
    }

    @PostMapping
    public Poll create(@RequestBody CreatePollDto dto) {
        // look up creator
        User creator = pm.findUser(dto.creatorId)
                .orElseThrow(() -> new RuntimeException("Creator not found"));

        // build poll
        Poll p = new Poll();
        p.setId(UUID.randomUUID());
        p.setQuestion(dto.question);
        p.setCreator(creator);
        p.setPublishedAt(dto.publishedAt);
        p.setValidUntil(dto.validUntil);

        // map option dto to domain options
        List<VoteOption> opts = new ArrayList<>();
        if (dto.options != null) {
            for (CreatePollDto.OptionIn in : dto.options) {
                VoteOption o = new VoteOption();
                o.setId(UUID.randomUUID());
                o.setCaption(in.caption);
                o.setPresentationOrder(in.presentationOrder);
                o.setPoll(p);
                opts.add(o);
            }
        }
        p.setOptions(opts);

        return pm.savePoll(p);
    }

    @GetMapping
    public Collection<Poll> list() {
        return pm.findAllPolls();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        pm.deletePoll(id);
    }

    @GetMapping("/{id}/counts")
    public Map<String, Long> getCounts(@PathVariable UUID id) {
        return cache.getOrLoad(id, () -> pm.computeVoteCounts(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Poll> getPoll(@PathVariable UUID id) {
        return pm.findPoll(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
