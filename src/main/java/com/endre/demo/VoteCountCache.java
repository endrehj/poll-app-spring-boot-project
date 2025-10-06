package com.endre.demo;

import org.springframework.stereotype.Component;
import redis.clients.jedis.UnifiedJedis;

import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Component
public class VoteCountCache {
    private static final int TTL_SECONDS = 300; // 5 minutes
    private final UnifiedJedis jedis;

    public VoteCountCache(UnifiedJedis jedis) {
        this.jedis = jedis;
    }

    private String key(UUID pollId) {
        return "poll:" + pollId + ":votes";
    }

    public Map<String, Long> getOrLoad(UUID pollId, Supplier<Map<String, Long>> loader) {
        String k = key(pollId);

        // try cache
        Map<String, String> raw = jedis.hgetAll(k);
        if (raw != null && !raw.isEmpty()) {
            return raw.entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, e -> Long.parseLong(e.getValue())));
        }

        // load
        Map<String, Long> fresh = loader.get();

        // cache + ttl
        if (fresh != null && !fresh.isEmpty()) {
            Map<String, String> toStore = fresh.entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, e -> String.valueOf(e.getValue())));
            jedis.hset(k, toStore);
            jedis.expire(k, TTL_SECONDS);
        }

        return fresh;
    }

    public void evict(UUID pollId) {
        jedis.del(key(pollId));
    }
}
