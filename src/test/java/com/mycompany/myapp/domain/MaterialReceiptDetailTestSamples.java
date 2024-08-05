package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MaterialReceiptDetailTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static MaterialReceiptDetail getMaterialReceiptDetailSample1() {
        return new MaterialReceiptDetail().id(1L).note("note1");
    }

    public static MaterialReceiptDetail getMaterialReceiptDetailSample2() {
        return new MaterialReceiptDetail().id(2L).note("note2");
    }

    public static MaterialReceiptDetail getMaterialReceiptDetailRandomSampleGenerator() {
        return new MaterialReceiptDetail().id(longCount.incrementAndGet()).note(UUID.randomUUID().toString());
    }
}
