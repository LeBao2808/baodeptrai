package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class OfferDetailTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static OfferDetail getOfferDetailSample1() {
        return new OfferDetail().id(1L).feedback("feedback1");
    }

    public static OfferDetail getOfferDetailSample2() {
        return new OfferDetail().id(2L).feedback("feedback2");
    }

    public static OfferDetail getOfferDetailRandomSampleGenerator() {
        return new OfferDetail().id(longCount.incrementAndGet()).feedback(UUID.randomUUID().toString());
    }
}
