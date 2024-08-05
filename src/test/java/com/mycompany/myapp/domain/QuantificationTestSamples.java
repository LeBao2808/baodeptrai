package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class QuantificationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Quantification getQuantificationSample1() {
        return new Quantification().id(1L);
    }

    public static Quantification getQuantificationSample2() {
        return new Quantification().id(2L);
    }

    public static Quantification getQuantificationRandomSampleGenerator() {
        return new Quantification().id(longCount.incrementAndGet());
    }
}
