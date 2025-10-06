package com.endre.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.CommandLineRunner;
import redis.clients.jedis.UnifiedJedis;

import java.util.Map;

@Configuration
public class RedisDemoConfig {

    @Bean
    CommandLineRunner redisDemoRunner() {
        return args -> {
            try (UnifiedJedis jedis = new UnifiedJedis("redis://localhost:6379")) {
                final String LOGGED_IN = "users:logged_in";
                jedis.del(LOGGED_IN);
                jedis.sadd(LOGGED_IN, "alice");
                jedis.sadd(LOGGED_IN, "bob");
                jedis.srem(LOGGED_IN, "alice");
                jedis.sadd(LOGGED_IN, "eve");
                System.out.println("Logged in: " + jedis.smembers(LOGGED_IN));

                String pollId = "demo-1";
                String metaKey  = "poll:" + pollId + ":meta";
                String votesKey = "poll:" + pollId + ":votes";
                jedis.hset(metaKey, Map.of("title", "Pineapple on pizza?"));
                jedis.hset(votesKey, Map.of(
                        "Yes, yammy!", "269",
                        "Mamma mia, nooooo!", "268",
                        "I do not really care ...", "42"
                ));
                jedis.hincrBy(votesKey, "Yes, yammy!", 1);
                System.out.println("Meta : " + jedis.hgetAll(metaKey));
                System.out.println("Votes: " + jedis.hgetAll(votesKey));
            }
        };
    }
}
