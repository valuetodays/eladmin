package me.zhengjie.utils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import cn.hutool.core.util.StrUtil;
import cn.vt.util.JsonUtils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RedissonClient;

/**
 * @author /
 */
@ApplicationScoped
@SuppressWarnings({"all"})
@Slf4j
public class RedisUtils {
    @Inject
    RedissonClient redissonClient;

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒) 注意:这里将会替换原有的时间
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redissonClient.getBucket(key).expire(Duration.ofMillis(time));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    /**
     * 指定缓存失效时间
     *
     * @param key      键
     * @param time     时间(秒) 注意:这里将会替换原有的时间
     * @param timeUnit 单位
     */
    public boolean expire(String key, long time, TimeUnit timeUnit) {
        try {
            if (time > 0) {
                redissonClient.getBucket(key).expire(time, timeUnit);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    /**
     * 根据 key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getExpire(String key) {
        return redissonClient.getBucket(key).remainTimeToLive();
    }

    /**
     * 查找匹配key
     *
     * @param pattern key
     * @return /
     */
    public List<String> scan(String pattern) {
        List<String> result = new ArrayList<>();
        Iterable<String> keys = redissonClient.getKeys().getKeysByPattern(pattern, 1000);
        for (String key : keys) {
            result.add(key);
        }
        return result;
    }


    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key) {
        try {
            boolean exists = redissonClient.getKeys().countExists("yourKey") > 0;
            return exists;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    public void del(String... keys) {
        if (keys != null && keys.length > 0) {
            if (keys.length == 1) {
                boolean result = redissonClient.getBucket(keys[0]).delete();
                log.debug("--------------------------------------------");
                log.debug(new StringBuilder("删除缓存：").append(keys[0]).append("，结果：").append(result).toString());
                log.debug("--------------------------------------------");
            } else {
                long count = redissonClient.getKeys().delete(keys);
                log.debug("--------------------------------------------");
                log.debug("成功删除缓存：" + StringUtils.joinWith(",", keys));
                log.debug("缓存删除数量：" + count + "个");
                log.debug("--------------------------------------------");
            }
        }
    }

    /**
     * 批量模糊删除key
     * @param pattern
     */
    public void scanDel(String pattern){
        while (true) {
            Iterable<String> keys = redissonClient.getKeys().getKeysByPattern(pattern, 1000);
            Iterator<String> iterator = keys.iterator();
            if (!iterator.hasNext()) {
                return;
            }
            for (String key : keys) {
                redissonClient.getKeys().delete(key);
            }
        }
    }

    // ============================String=============================

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        return key == null ? null : redissonClient.getBucket(key).get();
    }

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public <T> T get(String key, Class<T> clazz) {
        Object value = key == null ? null : redissonClient.getBucket(key).get();
        if (value == null) {
            return null;
        }
        // 如果 value 不是目标类型，则尝试将其反序列化为 clazz 类型
        if (!clazz.isInstance(value)) {
            return JsonUtils.toObject(value.toString(), clazz);
        } else if (clazz.isInstance(value)) {
            return clazz.cast(value);
        } else {
            return null;
        }
    }

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @param clazz 列表中元素的类型
     * @return 值
     */
    public <T> List<T> getList(String key, Class<T> clazz) {
        Object value = key == null ? null : redissonClient.getList(key).get();
        if (value == null) {
            return null;
        }
        if (value instanceof List<?>) {
            List<?> list = (List<?>) value;
            // 检查每个元素是否为指定类型
            if (list.stream().allMatch(clazz::isInstance)) {
                return list.stream().map(clazz::cast).collect(Collectors.toList());
            }
        }
        return null;
    }


    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public String getStr(String key) {
        if(StrUtil.isBlank(key)){
            return null;
        }
        Object value = redissonClient.getBucket(key).get();
        if (value == null) {
            return null;
        } else {
            return String.valueOf(value);
        }
    }

    /**
     * 批量获取
     *
     * @param keys
     * @return
     */
    public List<Object> multiGet(List<String> keys) {
        Map<String, Object> values = redissonClient.getBuckets().get(keys.toArray(String[]::new));
        if (MapUtils.isEmpty(values)) {
            return List.of();
        }
        return Arrays.asList(values.values().toArray());
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key, Object value) {
        int attempt = 0;
        while (attempt < 3) {
            try {
                redissonClient.getBucket(key).set(value);
                return true;
            } catch (Exception e) {
                attempt++;
                log.error("Attempt {} failed: {}", attempt, e.getMessage(), e);
            }
        }
        return false;
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期，注意:这里将会替换原有的时间
     * @return true成功 false 失败
     */
    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redissonClient.getBucket(key).set(value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key      键
     * @param value    值
     * @param time     时间，注意:这里将会替换原有的时间
     * @param timeUnit 类型
     * @return true成功 false 失败
     */
    public boolean set(String key, Object value, long time, TimeUnit timeUnit) {
        try {
            if (time > 0) {
                redissonClient.getBucket(key).set(value, time, timeUnit);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    // ================================Map=================================

    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public Object hget(String key, String item) {
        return redissonClient.getMap(key).get(item);
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public Map<Object, Object> hmget(String key) {
        return redissonClient.getMap(key).readAllMap();

    }

    /**
     * HashSet
     *
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    public boolean hmset(String key, Map<String, Object> map) {
        try {
            redissonClient.getMap(key).putAll(map);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * HashSet
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true成功 false失败
     */
    public boolean hmset(String key, Map<String, Object> map, long time) {
        try {
            redissonClient.getMap(key).putAll(map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false失败
     */
    public boolean hset(String key, String item, Object value) {
        try {
            redissonClient.getMap(key).put(item, value);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    public boolean hset(String key, String item, Object value, long time) {
        try {
            redissonClient.getMap(key).put(item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 删除hash表中的值
     *
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public void hdel(String key, Object... item) {
        redissonClient.getMap(key).fastRemove(item);
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public boolean hHasKey(String key, String item) {
        return redissonClient.getMap(key).containsKey(item);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     * @return
     */
    public double hincr(String key, String item, double by) {
        Object o = redissonClient.getMap(key).addAndGet(item, by);
        return o instanceof Long ? (Long) o : Long.valueOf(o.toString());
    }

    /**
     * hash递减
     *
     * @param key  键
     * @param item 项
     * @param by   要减少记(小于0)
     * @return
     */
    public double hdecr(String key, String item, double by) {
        return hincr(key, item, -by);
    }

    // ============================set=============================

    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return
     */
    public Set<Object> sGet(String key) {
        try {
            return redissonClient.getSet(key).readAll();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public boolean sHasKey(String key, Object value) {
        try {
            return redissonClient.getSet(key).contains(value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     */
    public long sSet(String key, Object... values) {
        try {
            redissonClient.getSet(key).add(values);
            return 1;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return 0;
        }
    }

    /**
     * 将set数据放入缓存
     *
     * @param key    键
     * @param time   时间(秒) 注意:这里将会替换原有的时间
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSetAndTime(String key, long time, Object... values) {
        try {
            redissonClient.getSet(key).add(values);
            if (time > 0) {
                expire(key, time);
            }
            return 1;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return 0;
        }
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return
     */
    public long sGetSetSize(String key) {
        try {
            return redissonClient.getSet(key).size();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return 0;
        }
    }

    /**
     * 移除值为value的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public long setRemove(String key, Object... values) {
        try {
            redissonClient.getSet(key).removeAll(Arrays.asList(values));
            return 1;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return 0;
        }
    }

    // ===============================list=================================

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束 0 到 -1代表所有值
     * @return
     */
    public List<Object> lGet(String key, int start, int end) {
        try {
            return redissonClient.getList(key).range(start, end);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return
     */
    public long lGetListSize(String key) {
        try {
            return redissonClient.getList(key).size();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return 0;
        }
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    public Object lGetIndex(String key, int index) {
        try {
            return redissonClient.getList(key).get(index);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public boolean lSet(String key, Object value) {
        try {
            redissonClient.getList(key).addLast(value);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) 注意:这里将会替换原有的时间
     * @return
     */
    public boolean lSet(String key, Object value, long time) {
        try {
            redissonClient.getList(key).addLast(value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public boolean lSet(String key, List<Object> value) {
        try {
            redissonClient.getList(key).addAll(value);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) 注意:这里将会替换原有的时间
     * @return
     */
    public boolean lSet(String key, List<Object> value, long time) {
        try {
            redissonClient.getList(key).addAll(value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return /
     */
    public boolean lUpdateIndex(String key, int index, Object value) {
        try {
            redissonClient.getList(key).set(index, value);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public long lRemove(String key, int count, Object value) {
        try {
            redissonClient.getList(key).remove(value, count);
            return 1;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return 0;
        }
    }

    /**
     * @param prefix 前缀
     * @param ids    id
     */
    public void delByKeys(String prefix, Set<Long> ids) {
        Set<String> keys = new HashSet<>();
        for (Long id : ids) {
            keys.add(new StringBuffer(prefix).append(id).toString());
        }
        long count = redissonClient.getKeys().delete(keys.toArray(String[]::new));
    }

    // ============================incr=============================

    /**
     * 递增
     * @param key
     * @return
     */
    public Long increment(String key) {
        return increment(key, 1L);
    }

    /**
     * 递增
     * @param key
     * @return
     */
    public Long increment(String key, Long delta) {
        return redissonClient.getAtomicLong(key).addAndGet(delta);
    }

    /**
     * 递减
     * @param key
     * @return
     */
    public Long decrement(String key) {
        return increment(key, -1L);
    }
}
