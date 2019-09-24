package wen.datacache.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import wen.datacache.mapper.RedisMapper;

@RestController
@RequestMapping("/redis")
public class CityInfoController {

    private RedisMapper redisMapper;

    @Autowired
    public CityInfoController(RedisMapper redisMapper) {
        this.redisMapper = redisMapper;
    }

    @RequestMapping(value = "get/{key}", method = RequestMethod.GET)
    public String find(@PathVariable("key") String key) {
        return redisMapper.getValue(key);
    }

    @RequestMapping(value = "add/{key}/{value}", method = RequestMethod.GET)
    public Boolean add(@PathVariable("key") String key, @PathVariable("value") String value) {
        return redisMapper.cacheValue(key, value);
    }

}
