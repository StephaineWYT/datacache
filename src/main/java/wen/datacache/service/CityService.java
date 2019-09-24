package wen.datacache.service;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import wen.datacache.entity.CityInfo;

@Component
@CacheConfig(cacheNames = "CityService")
public class CityService {

    @Cacheable
    public CityInfo getCity(int id, String city) {
        return new CityInfo(id, city);
    }
}
