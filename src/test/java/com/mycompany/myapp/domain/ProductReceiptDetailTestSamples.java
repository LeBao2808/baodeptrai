package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ProductReceiptDetailTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ProductReceiptDetail getProductReceiptDetailSample1() {
        return new ProductReceiptDetail().id(1L).note("note1").quantity(1);
    }

    public static ProductReceiptDetail getProductReceiptDetailSample2() {
        return new ProductReceiptDetail().id(2L).note("note2").quantity(2);
    }

    public static ProductReceiptDetail getProductReceiptDetailRandomSampleGenerator() {
        return new ProductReceiptDetail()
            .id(longCount.incrementAndGet())
            .note(UUID.randomUUID().toString())
            .quantity(intCount.incrementAndGet());
    }
}
