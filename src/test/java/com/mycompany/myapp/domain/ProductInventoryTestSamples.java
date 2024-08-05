package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ProductInventoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ProductInventory getProductInventorySample1() {
        return new ProductInventory().id(1L).inventoryMonth(1).inventoryYear(1).type(1);
    }

    public static ProductInventory getProductInventorySample2() {
        return new ProductInventory().id(2L).inventoryMonth(2).inventoryYear(2).type(2);
    }

    public static ProductInventory getProductInventoryRandomSampleGenerator() {
        return new ProductInventory()
            .id(longCount.incrementAndGet())
            .inventoryMonth(intCount.incrementAndGet())
            .inventoryYear(intCount.incrementAndGet())
            .type(intCount.incrementAndGet());
    }
}
