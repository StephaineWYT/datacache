package wen.datacache.mapper.impl;

import wen.datacache.mapper.RedisMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Repository
public class RedisMapperImpl implements RedisMapper {

    private static final String KEY_PREFIX_VALUE = "wen:datacache:value:";
    private static final String KEY_PREFIX_SET = "wen:datacache:set:";
    private static final String KEY_PREFIX_LIST = "wen:datacache:list:";

    private final RedisTemplate<String, String> redisTemplate;

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public RedisMapperImpl(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean cacheValue(String k, String v, Long time) {
        String key = KEY_PREFIX_VALUE + k;
        try {
            ValueOperations<String, String> valueOps = redisTemplate.opsForValue();
            valueOps.set(key, v);
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Throwable t) {
            LOGGER.error("缓存[" + key + "]失败, value[" + v + "]", t);
        }
        return false;
    }

    @Override
    public boolean cacheValue(String k, String v) {
        return cacheValue(k, v, -1L);
    }

    @Override
    public boolean containsValueKey(String k) {
        return containsKey(KEY_PREFIX_VALUE + k);
    }

    @Override
    public boolean containsSetKey(String k) {
        return containsKey(KEY_PREFIX_SET + k);
    }

    @Override
    public boolean containsListKey(String k) {
        return containsKey(KEY_PREFIX_LIST + k);
    }

    @Override
    public boolean containsKey(String k) {
        try {
            return redisTemplate.hasKey(k);
        } catch (Throwable t) {
            LOGGER.error("判断缓存存在失败key[" + k + ", Code or[" + t + "]");
        }
        return false;
    }

    @Override
    public String getValue(String k) {
        try {
            ValueOperations<String, String> valueOps = redisTemplate.opsForValue();
            return valueOps.get(KEY_PREFIX_VALUE + k);
        } catch (Throwable t) {
            LOGGER.error("获取缓存失败key[" + KEY_PREFIX_VALUE + k + ", Code or[" + t + "]");
        }
        return null;
    }

    @Override
    public boolean removeValue(String k) {
        return remove(KEY_PREFIX_VALUE + k);
    }

    @Override
    public boolean removeSet(String k) {
        return remove(KEY_PREFIX_SET + k);
    }

    @Override
    public boolean removeList(String k) {
        return remove(KEY_PREFIX_LIST + k);
    }

    @Override
    public boolean cacheSet(String k, String v, long time) {
        String key = KEY_PREFIX_SET + k;
        try {
            SetOperations<String, String> setOps = redisTemplate.opsForSet();
            setOps.add(key, v);
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Throwable t) {
            LOGGER.error("缓存[" + key + "]失败, value[" + v + "]", t);
        }
        return false;
    }

    @Override
    public boolean cacheSet(String k, String v) {
        return cacheSet(k, v, -1);
    }

    @Override
    public boolean cacheSet(String k, Set<String> v, long time) {
        String key = KEY_PREFIX_SET + k;
        try {
            SetOperations<String, String> setOps = redisTemplate.opsForSet();
            setOps.add(key, v.toArray(new String[v.size()]));
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Throwable t) {
            LOGGER.error("缓存[" + key + "]失败, value[" + v + "]", t);
        }
        return false;
    }

    @Override
    public boolean cacheSet(String k, Set<String> v) {
        return cacheSet(k, v, -1);
    }

    @Override
    public Set<String> getSet(String k) {
        try {
            SetOperations<String, String> setOps = redisTemplate.opsForSet();
            return setOps.members(KEY_PREFIX_SET + k);
        } catch (Throwable t) {
            LOGGER.error("获取set缓存失败key[" + KEY_PREFIX_SET + k + ", Codeor[" + t + "]");
        }
        return null;
    }

    @Override
    public boolean cacheList(String k, String v, long time) {
        String key = KEY_PREFIX_LIST + k;
        try {
            ListOperations<String, String> listOps = redisTemplate.opsForList();
            listOps.rightPush(key, v);
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Throwable t) {
            LOGGER.error("缓存[" + key + "]失败, value[" + v + "]", t);
        }
        return false;
    }

    @Override
    public boolean cacheList(String k, String v) {
        return cacheList(k, v, -1);
    }

    @Override
    public boolean cacheList(String k, List<String> v, long time) {
        String key = KEY_PREFIX_LIST + k;
        try {
            ListOperations<String, String> listOps = redisTemplate.opsForList();
            listOps.rightPushAll(key, v);
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Throwable t) {
            LOGGER.error("缓存[" + key + "]失败, value[" + v + "]", t);
        }
        return false;
    }

    @Override
    public boolean cacheList(String k, List<String> v) {
        return cacheList(k, v, -1);
    }

    @Override
    public List<String> getList(String k, long start, long end) {
        try {
            ListOperations<String, String> listOps = redisTemplate.opsForList();
            return listOps.range(KEY_PREFIX_LIST + k, start, end);
        } catch (Throwable t) {
            LOGGER.error("获取list缓存失败key[" + KEY_PREFIX_LIST + k + ", Code or[" + t + "]");
        }
        return null;
    }

    @Override
    public long getListSize(String k) {
        try {
            ListOperations<String, String> listOps = redisTemplate.opsForList();
            return listOps.size(KEY_PREFIX_LIST + k);
        } catch (Throwable t) {
            LOGGER.error("获取list长度失败key[" + KEY_PREFIX_LIST + k + "], Code or[" + t + "]");
        }
        return 0;
    }

    @Override
    public long getListSize(ListOperations<String, String> listOps, String k) {
        try {
            return listOps.size(KEY_PREFIX_LIST + k);
        } catch (Throwable t) {
            LOGGER.error("获取list长度失败key[" + KEY_PREFIX_LIST + k + "], Code or[" + t + "]");
        }
        return 0;
    }

    @Override
    public boolean removeOneOfList(String k) {
        String key = KEY_PREFIX_LIST + k;
        try {
            ListOperations<String, String> listOps = redisTemplate.opsForList();
            listOps.rightPop(key);
            return true;
        } catch (Throwable t) {
            LOGGER.error("移除list缓存失败key[" + KEY_PREFIX_LIST + k + ", Code or[" + t + "]");
        }
        return false;
    }

    private boolean remove(String k) {
        try {
            redisTemplate.delete(k);
            return true;
        } catch (Throwable t) {
            LOGGER.error("获取缓存失败key[" + k + ", Code or[" + t + "]");
        }
        return false;
    }
}
