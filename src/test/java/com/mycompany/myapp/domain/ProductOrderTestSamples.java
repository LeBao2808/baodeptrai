package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ProductOrderTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ProductOrder getProductOrderSample1() {
        return new ProductOrder().id(1L).paymentMethod("paymentMethod1").note("note1").status(1).code("code1");
    }

    public static ProductOrder getProductOrderSample2() {
        return new ProductOrder().id(2L).paymentMethod("paymentMethod2").note("note2").status(2).code("code2");
    }

    public static ProductOrder getProductOrderRandomSampleGenerator() {
        return new ProductOrder()
            .id(longCount.incrementAndGet())
            .paymentMethod(UUID.randomUUID().toString())
            .note(UUID.randomUUID().toString())
            .status(intCount.incrementAndGet())
            .code(UUID.randomUUID().toString());
    }
}
