package com.misrshoryanelataa.misr_shoryan_elataa.Security;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class LoginRateLimiter {

    private static final int MAX_ATTEMPTS = 3;
    private static final long BLOCK_TIME = 1 * 60 * 1000L;

    private static class Data {
        int attempts = 0;
        long blockUntil = 0;
    }

    private final Map<String, Data> store = new ConcurrentHashMap<>();

    public boolean isBlocked(String key) {
        Data d = store.get(key);
        if (d == null) return false;

        if (d.blockUntil == 0) return false;

        if (System.currentTimeMillis() > d.blockUntil) {
            store.remove(key);
            return false;
        }

        return true;
    }

   
    public void recordFailure(String key) {
        store.compute(key, (k, d) -> {
            if (d == null) d = new Data();

            d.attempts++;

            if (d.attempts >= MAX_ATTEMPTS) {
                d.blockUntil = System.currentTimeMillis() + BLOCK_TIME;
            }

            return d;
        });
    }


    public void reset(String key) {
        store.remove(key);
    }

    public int remainingAttempts(String key) {
        Data d = store.get(key);
        if (d == null) return MAX_ATTEMPTS;

        return Math.max(0, MAX_ATTEMPTS - d.attempts);
    }



    public long remainingBlockSeconds(String key) {
        Data d = store.get(key);
        if (d == null) return 0;

        long remaining = d.blockUntil - System.currentTimeMillis();
        return remaining > 0 ? (long) Math.ceil(remaining / 1000.0) : 0;
    }


    public long remainingBlockMinutes(String key) {
        Data d = store.get(key);
        if (d == null) return 0;

        long remaining = d.blockUntil - System.currentTimeMillis();
        return remaining > 0 ? (long) Math.ceil(remaining / 60000.0) : 0;
    }
}