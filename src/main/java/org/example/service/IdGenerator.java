package org.example.service;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Component;

@Component
public class IdGenerator {

    private final AtomicInteger merchant = new AtomicInteger();
    private final AtomicLong terminal = new AtomicLong();

    public String nextMerchant() {
        int id = Math.floorMod(merchant.getAndIncrement(), 500_000);
        return "M%07d".formatted(id);
    }

    public String nextTerminal() {
        long id = terminal.getAndIncrement();
        return "T%07d".formatted(id);
    }

}
