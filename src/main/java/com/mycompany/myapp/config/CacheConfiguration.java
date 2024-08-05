package com.mycompany.myapp.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(
                Object.class,
                Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries())
            )
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build()
        );
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.mycompany.myapp.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.mycompany.myapp.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.mycompany.myapp.domain.Authority.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Config.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Product.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Supplier.class.getName());
            createCache(cm, com.mycompany.myapp.domain.ProductionSite.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Material.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Quantification.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Planning.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Customer.class.getName());
            createCache(cm, com.mycompany.myapp.domain.ProductOrder.class.getName());
            createCache(cm, com.mycompany.myapp.domain.ProductOrderDetail.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Offer.class.getName());
            createCache(cm, com.mycompany.myapp.domain.OfferDetail.class.getName());
            createCache(cm, com.mycompany.myapp.domain.MaterialReceipt.class.getName());
            createCache(cm, com.mycompany.myapp.domain.MaterialReceiptDetail.class.getName());
            createCache(cm, com.mycompany.myapp.domain.MaterialExport.class.getName());
            createCache(cm, com.mycompany.myapp.domain.MaterialExportDetail.class.getName());
            createCache(cm, com.mycompany.myapp.domain.MaterialInventory.class.getName());
            createCache(cm, com.mycompany.myapp.domain.ProductReceipt.class.getName());
            createCache(cm, com.mycompany.myapp.domain.ProductReceiptDetail.class.getName());
            createCache(cm, com.mycompany.myapp.domain.ProductInventory.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
