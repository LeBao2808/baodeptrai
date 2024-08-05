package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MaterialExportDetailTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static MaterialExportDetail getMaterialExportDetailSample1() {
        return new MaterialExportDetail().id(1L).note("note1");
    }

    public static MaterialExportDetail getMaterialExportDetailSample2() {
        return new MaterialExportDetail().id(2L).note("note2");
    }

    public static MaterialExportDetail getMaterialExportDetailRandomSampleGenerator() {
        return new MaterialExportDetail().id(longCount.incrementAndGet()).note(UUID.randomUUID().toString());
    }
}
