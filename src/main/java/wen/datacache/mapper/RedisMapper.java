package wen.datacache.mapper;

import org.springframework.data.redis.core.ListOperations;
import java.util.List;
import java.util.Set;

public interface RedisMapper {

    boolean cacheValue(String k, String v, Long time);

    boolean cacheValue(String k, String v);

    boolean containsValueKey(String k);

    boolean containsSetKey(String k);

    boolean containsListKey(String k);

    boolean containsKey(String k);

    String getValue(String k);

    boolean removeValue(String k);

    boolean removeSet(String k);

    boolean removeList(String k);

    boolean cacheSet(String k, String v, long time);

    boolean cacheSet(String k, String v);

    boolean cacheSet(String k, Set<String> v, long time);

    boolean cacheSet(String k, Set<String> v);

    Set<String> getSet(String k);

    boolean cacheList(String k, String v, long time);

    boolean cacheList(String k, String v);

    boolean cacheList(String k, List<String> v, long time);

    boolean cacheList(String k, List<String> v);

    List<String> getList(String k, long start, long end);

    long getListSize(String k);

    long getListSize(ListOperations<String, String> listOps, String k);

    boolean removeOneOfList(String k);
}
