package org.example.service;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Component;

@Component
public class MaxRecorder {

    private final AtomicLong max = new AtomicLong(-1);
    private final AtomicLong sum = new AtomicLong(0);
    private final AtomicLong count = new AtomicLong(0);
    private final AtomicLong errors = new AtomicLong(0);

    // Наносекунды
    private final AtomicLong maxNanos = new AtomicLong(-1);
    private final AtomicLong sumNanos = new AtomicLong(0);

    /** Записать одно измерение в миллисекундах */
    public void record(Long millis) {
        if (millis == null) {
            return;
        }
        max.updateAndGet(prev -> Math.max(prev, millis));
        sum.addAndGet(millis);
        count.incrementAndGet();
    }

    /** Записать одно измерение в наносекундах */
    public void recordNanos(Long nanos) {
        if (nanos == null) {
            return;
        }
        maxNanos.updateAndGet(prev -> Math.max(prev, nanos));
        sumNanos.addAndGet(nanos);
        count.incrementAndGet();
    }

    /** Записать измерение с наносекундами и миллисекундами одновременно */
    public void recordBoth(Long millis, Long nanos) {
        if (nanos == null || millis == null) {
            return;
        }
        maxNanos.updateAndGet(prev -> Math.max(prev, nanos));
        sumNanos.addAndGet(nanos);
        max.updateAndGet(prev -> Math.max(prev, millis));
        sum.addAndGet(millis);
        count.incrementAndGet();
    }

    /** Зафиксировать ошибку */
    public void incrementErrors() {
        errors.incrementAndGet();
    }

    // Методы для миллисекунд
    public long getMax() {return max.get();}

    public double getAvg() {
        long c = count.get();
        return c == 0 ? 0.0 : ((double) sum.get()) / c;
    }

    // Методы для наносекунд
    public long getMaxNanos() {return maxNanos.get();}

    public double getAvgNanos() {
        long c = count.get();
        return c == 0 ? 0.0 : ((double) sumNanos.get()) / c;
    }

    // Конвертация наносекунд в миллисекунды
    public double getAvgNanosAsMillis() {
        return getAvgNanos() / 1_000_000.0;
    }

    public long getMaxNanosAsMillis() {
        return maxNanos.get() / 1_000_000;
    }

    // Общие методы
    public long getCount() {return count.get();}

    public long getErrors() {return errors.get();}

    public void reset() {
        max.set(-1);
        sum.set(0);
        maxNanos.set(-1);
        sumNanos.set(0);
        count.set(0);
        errors.set(0);
    }

    // Метод для красивого вывода статистики
    public String getStats() {
        return String.format(
                "Count: %d, Errors: %d | Max: %dms (%.2fμs) | Avg: %.2fms (%.2fμs)",
                getCount(),
                getErrors(),
                getMax(),
                getMaxNanos() / 1000.0, // наносекунды в микросекунды
                getAvg(),
                getAvgNanos() / 1000.0  // наносекунды в микросекунды
        );
    }
}