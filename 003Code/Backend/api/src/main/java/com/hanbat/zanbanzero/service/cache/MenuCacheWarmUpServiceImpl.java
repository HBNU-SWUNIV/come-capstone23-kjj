package com.hanbat.zanbanzero.service.cache;

import com.hanbat.zanbanzero.service.menu.MenuService;
import com.hanbat.zanbanzero.service.menu.MenuServiceImplV1;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuCacheWarmUpServiceImpl implements CacheWarmUpService{

    private final MenuService menuService;
    private final CacheManager cacheManager;

    @Override
    @PostConstruct
    public void warmUpCache() {
        String value = MenuServiceImplV1.ALL_FOODS_CACHE_VALUE;
        String key = MenuServiceImplV1.ALL_FOODS_CACHE_KEY;
        Cache cache = cacheManager.getCache(value);
        cache.put(key, menuService.getFood());
    }
}