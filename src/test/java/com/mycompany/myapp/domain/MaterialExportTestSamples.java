package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MaterialExportTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static MaterialExport getMaterialExportSample1() {
        return new MaterialExport().id(1L).status(1).code("code1");
    }

    public static MaterialExport getMaterialExportSample2() {
        return new MaterialExport().id(2L).status(2).code("code2");
    }

    public static MaterialExport getMaterialExportRandomSampleGenerator() {
        return new MaterialExport().id(longCount.incrementAndGet()).status(intCount.incrementAndGet()).code(UUID.randomUUID().toString());
    }
}
