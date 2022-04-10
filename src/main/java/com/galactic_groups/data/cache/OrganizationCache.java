package com.galactic_groups.data.cache;

import com.galactic_groups.data.model.Organization;
import com.galactic_groups.data.repository.OrganizationRepository;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class OrganizationCache {
    private final LoadingCache<Integer, Organization> cache;

    OrganizationCache(OrganizationRepository organizationRepository) {
        cache = CacheBuilder.newBuilder()
                .expireAfterWrite(1, TimeUnit.DAYS)
                .build(CacheLoader.from(organizationRepository::getById));
    }

    @SneakyThrows
    public Organization getById(int id) {
        return cache.get(id);
    }

    public void invalidateById(int id) {
        cache.invalidate(id);
    }
}
