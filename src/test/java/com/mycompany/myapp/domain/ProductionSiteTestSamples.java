package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ProductionSiteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ProductionSite getProductionSiteSample1() {
        return new ProductionSite().id(1L).name("name1").address("address1").phone("phone1");
    }

    public static ProductionSite getProductionSiteSample2() {
        return new ProductionSite().id(2L).name("name2").address("address2").phone("phone2");
    }

    public static ProductionSite getProductionSiteRandomSampleGenerator() {
        return new ProductionSite()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .address(UUID.randomUUID().toString())
            .phone(UUID.randomUUID().toString());
    }
}
