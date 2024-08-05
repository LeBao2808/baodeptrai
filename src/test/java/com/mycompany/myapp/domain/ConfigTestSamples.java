package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ConfigTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Config getConfigSample1() {
        return new Config().id(1L).key("key1").value("value1");
    }

    public static Config getConfigSample2() {
        return new Config().id(2L).key("key2").value("value2");
    }

    public static Config getConfigRandomSampleGenerator() {
        return new Config().id(longCount.incrementAndGet()).key(UUID.randomUUID().toString()).value(UUID.randomUUID().toString());
    }
}
