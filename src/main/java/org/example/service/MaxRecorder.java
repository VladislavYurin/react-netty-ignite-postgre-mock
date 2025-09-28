package org.example.service;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Component;

@Component
public class MaxRecorder {

    private final AtomicLong max = new AtomicLong(-1);
    private final AtomicLong sum = new AtomicLong(0);
    private final AtomicLong count = new AtomicLong(0);
    private final AtomicLong errors = new AtomicLong(0); // <-- новый счётчик

    /** Записать одно измерение в миллисекундах */
    public void record(Long millis) {
        if (millis == null) {
            return;
        }
        max.updateAndGet(prev -> Math.max(prev, millis));
        sum.addAndGet(millis);
        count.incrementAndGet();
    }

    /** Зафиксировать ошибку */
    public void incrementErrors() {
        errors.incrementAndGet();
    }

    public long getMax() {return max.get();}

    public double getAvg() {
        long c = count.get();
        return c == 0 ? 0.0 : ((double) sum.get()) / c;
    }

    public long getCount() {return count.get();}

    public long getErrors() {return errors.get();}

    public void reset() {
        max.set(-1);
        sum.set(0);
        count.set(0);
        errors.set(0);
    }

}
