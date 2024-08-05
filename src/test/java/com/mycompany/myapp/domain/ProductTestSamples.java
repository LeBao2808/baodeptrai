package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ProductTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Product getProductSample1() {
        return new Product()
            .id(1L)
            .name("name1")
            .code("code1")
            .unit("unit1")
            .description("description1")
            .imageUrl("imageUrl1")
            .type(1)
            .color("color1")
            .construction(1)
            .masterPackingQty(1)
            .masterNestCode(1)
            .innerQty(1)
            .packSize(1)
            .nestCode("nestCode1")
            .countryOfOrigin("countryOfOrigin1")
            .vendorName("vendorName1")
            .numberOfSet(1)
            .qty40HC(1)
            .d57Qty(1)
            .category("category1")
            .labels("labels1")
            .planningNotes("planningNotes1")
            .factTag("factTag1");
    }

    public static Product getProductSample2() {
        return new Product()
            .id(2L)
            .name("name2")
            .code("code2")
            .unit("unit2")
            .description("description2")
            .imageUrl("imageUrl2")
            .type(2)
            .color("color2")
            .construction(2)
            .masterPackingQty(2)
            .masterNestCode(2)
            .innerQty(2)
            .packSize(2)
            .nestCode("nestCode2")
            .countryOfOrigin("countryOfOrigin2")
            .vendorName("vendorName2")
            .numberOfSet(2)
            .qty40HC(2)
            .d57Qty(2)
            .category("category2")
            .labels("labels2")
            .planningNotes("planningNotes2")
            .factTag("factTag2");
    }

    public static Product getProductRandomSampleGenerator() {
        return new Product()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .code(UUID.randomUUID().toString())
            .unit(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .imageUrl(UUID.randomUUID().toString())
            .type(intCount.incrementAndGet())
            .color(UUID.randomUUID().toString())
            .construction(intCount.incrementAndGet())
            .masterPackingQty(intCount.incrementAndGet())
            .masterNestCode(intCount.incrementAndGet())
            .innerQty(intCount.incrementAndGet())
            .packSize(intCount.incrementAndGet())
            .nestCode(UUID.randomUUID().toString())
            .countryOfOrigin(UUID.randomUUID().toString())
            .vendorName(UUID.randomUUID().toString())
            .numberOfSet(intCount.incrementAndGet())
            .qty40HC(intCount.incrementAndGet())
            .d57Qty(intCount.incrementAndGet())
            .category(UUID.randomUUID().toString())
            .labels(UUID.randomUUID().toString())
            .planningNotes(UUID.randomUUID().toString())
            .factTag(UUID.randomUUID().toString());
    }
}
