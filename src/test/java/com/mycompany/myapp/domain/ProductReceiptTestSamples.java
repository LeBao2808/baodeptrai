package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ProductReceiptTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ProductReceipt getProductReceiptSample1() {
        return new ProductReceipt().id(1L).status(1).code("code1");
    }

    public static ProductReceipt getProductReceiptSample2() {
        return new ProductReceipt().id(2L).status(2).code("code2");
    }

    public static ProductReceipt getProductReceiptRandomSampleGenerator() {
        return new ProductReceipt().id(longCount.incrementAndGet()).status(intCount.incrementAndGet()).code(UUID.randomUUID().toString());
    }
}
