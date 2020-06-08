package com.example.zg.redis;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * @author zhangguo
 * @date 2020/6/2 18:45
 * @description StringRedisTemplate工具类
 * 针对所有的hash 都是以h开头的方法
 * 针对所有的Set 都是以s开头的方法
 * 针对所有的List 都是以l开头的方法
 */
@Slf4j
@Component
public class StringRedisService<T> {

    @Resource
    private StringRedisTemplate stringRedisTemplate;



    //=============================common============================

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     * @return
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                stringRedisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     * @param timeUnit 时间单位
     * @return
     */
    public boolean expire(String key, long time, TimeUnit timeUnit) {
        try {
            if (time > 0) {
                stringRedisTemplate.expire(key, time, timeUnit);
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public Long getExpire(String key) {
        return stringRedisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public Boolean hasKey(String key) {
        try {
            return stringRedisTemplate.hasKey(key);
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                stringRedisTemplate.delete(key[0]);
            } else {
                stringRedisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    //============================String=============================

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public T get(String key, Class<T> clazz) {
        return key == null ? null : JSON.parseObject(stringRedisTemplate.opsForValue().get(key), clazz);
    }

//    public T get(String key) {
//        return key == null ? null : JSON.parseObject(stringRedisTemplate.opsForValue().get(key), T.class);
//    }
//
    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key, T value) {
        try {
            stringRedisTemplate.opsForValue().set(key, JSON.toJSONString(value));
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }

    }
//
//    /**
//     * 普通缓存放入并设置时间
//     *
//     * @param key   键
//     * @param value 值
//     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
//     * @return true成功 false 失败
//     */
//    public boolean set(String key, T value, long time) {
//        try {
//            if (time > 0) {
//                stringRedisTemplate.opsForValue().set(key, JSON.toJSONString(value), time, TimeUnit.SECONDS);
//            } else {
//                set(key, value);
//            }
//            return true;
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            return false;
//        }
//    }
//
//    //================================Map=================================
//
//    /**
//     * HashGet
//     *
//     * @param key  键 不能为null
//     * @param item 项 不能为null
//     * @param clazz class对象
//     * @return 值
//     */
//    public T hget(String key, String item, Class<T> clazz) {
//        return JSON.parseObject((String) stringRedisTemplate.opsForHash().get(key, item), clazz);
//    }
//
//    /**
//     * 获取hashKey对应的所有键值
//     *
//     * @param key 键
//     * @return 对应的多个键值
//     */
//    public Map<String, T> hmget(String key, Class<T> clazz) {
//        Map<String, T> result = new HashMap<>();
//        for (Map.Entry<Object, Object> entry : stringRedisTemplate.opsForHash().entries(key).entrySet()) {
//            result.put(entry.getKey().toString(), JSON.parseObject(entry.getValue().toString(), clazz));
//        }
//        return result;
//    }
//
//    /**
//     * HashSet
//     *
//     * @param key 键
//     * @param map 对应多个键值
//     * @return true 成功 false 失败
//     */
//    public boolean hmset(String key, Map<String, T> map) {
//        try {
//            map.forEach((key1, value) -> hset(key, key1, value));
//            return true;
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            return false;
//        }
//    }
//
//    /**
//     * HashSet 并设置时间
//     *
//     * @param key  键
//     * @param map  对应多个键值
//     * @param time 时间(秒)
//     * @return true成功 false失败
//     */
//    public boolean hmset(String key, Map<String, T> map, long time) {
//        try {
//            map.forEach((key1, value) -> hset(key, key1, value));
//            if (time > 0) {
//                expire(key, time);
//            }
//            return true;
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            return false;
//        }
//    }
//
//    /**
//     * 向一张hash表中放入数据,如果不存在将创建
//     *
//     * @param key   键
//     * @param item  项
//     * @param value 值
//     * @return true 成功 false失败
//     */
//    public boolean hset(String key, String item, T value) {
//        try {
//            stringRedisTemplate.opsForHash().put(key, item, JSON.toJSONString(value));
//            return true;
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            return false;
//        }
//    }
//
//    /**
//     * 删除hash表中的值
//     *
//     * @param key  键 不能为null
//     * @param item 项 可以使多个 不能为null
//     */
//    public void hdel(String key, Object... item) {
//        stringRedisTemplate.opsForHash().delete(key, item);
//    }
//
//        /**
//     * 判断hash表中是否有该项的值
//     *
//     * @param key  键 不能为null
//     * @param item 项 不能为null
//     * @return true 存在 false不存在
//     */
//    public boolean hHasKey(String key, String item) {
//        return stringRedisTemplate.opsForHash().hasKey(key, item);
//    }
//
//
//    //============================set=============================
//
//    /**
//     * 根据key获取Set中的所有值
//     *
//     * @param key 键
//     * @return
//     */
//    public Set<T> sGet(String key, Class<T> clazz) {
//        try {
//            return Objects.requireNonNull(stringRedisTemplate.opsForSet().members(key)).stream().map(json->JSON.parseObject(json, clazz)).collect(Collectors.toSet());
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            return Collections.emptySet();
//        }
//    }
//
//    /**
//     * 根据value从一个set中查询,是否存在
//     *
//     * @param key   键
//     * @param value 值
//     * @return true 存在 false不存在
//     */
//    public Boolean sHasKey(String key, T value) {
//        try {
//            return stringRedisTemplate.opsForSet().isMember(key, JSON.toJSONString(value));
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            return false;
//        }
//    }
//
//    /**
//     * 将数据放入set缓存
//     *
//     * @param key    键
//     * @param values 值 可以是多个
//     * @return 成功个数
//     */
//    public Long sSet(String key, T... values) {
//        try {
//            String[] valueArray = new String[values.length];
//            for (int i = 0; i < values.length; i++) {
//                valueArray[i] = JSON.toJSONString(values[i]);
//            }
//            return stringRedisTemplate.opsForSet().add(key, valueArray);
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            return 0L;
//        }
//    }
//
//    /**
//     * 将数据放入set缓存
//     *
//     * @param key    键
//     * @param values 值
//     * @return 成功个数
//     */
//    public Long sSet(String key, Set<T> values) {
//        try {
//            String[] valueArray = new String[values.size()];
//            int i = 0;
//            for (T value : values) {
//                valueArray[i++] = JSON.toJSONString(value);
//            }
//            return stringRedisTemplate.opsForSet().add(key, valueArray);
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            return 0L;
//        }
//    }
//
//    /**
//     * 将set数据放入缓存
//     *
//     * @param key    键
//     * @param time   时间(秒)
//     * @param values 值 可以是多个
//     * @return 成功个数
//     */
//    public Long sSetAndTime(String key, long time, T... values) {
//        try {
//            String[] valueArray = new String[values.length];
//            for (int i = 0; i < values.length; i++) {
//                valueArray[i] = JSON.toJSONString(values[i]);
//            }
//            Long count = stringRedisTemplate.opsForSet().add(key, valueArray);
//            if (time > 0) {
//                expire(key, time);
//            }
//            return count;
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            return 0L;
//        }
//    }
//
//    /**
//     * 获取set缓存的长度
//     *
//     * @param key 键
//     * @return
//     */
//    public Long sGetSetSize(String key) {
//        try {
//            return stringRedisTemplate.opsForSet().size(key);
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            return 0L;
//        }
//    }
//
//    /**
//     * 移除值为value的
//     *
//     * @param key    键
//     * @param values 值 可以是多个
//     * @return 移除的个数
//     */
//    public Long sRemove(String key, T... values) {
//        try {
//            Object[] valueArray = new String[values.length];
//            for (int i = 0; i < values.length; i++) {
//                valueArray[i] = JSON.toJSONString(values[i]);
//            }
//            return stringRedisTemplate.opsForSet().remove(key, valueArray);
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            return 0L;
//        }
//    }
//
////    ===============================list=================================
//
//    /**
//     * 获取list缓存的内容
//     *
//     * @param key   键
//     * @param start 开始
//     * @param end   结束  0 到 -1代表所有值
//     * @return
//     */
//    public List<T> lGet(String key, long start, long end, Class<T> clazz) {
//        try {
//            return Objects.requireNonNull(stringRedisTemplate.opsForList().range(key, start, end)).stream().map(json->JSON.parseObject(json, clazz)).collect(Collectors.toList());
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            return Collections.emptyList();
//        }
//    }
//
//    /**
//     * 获取list缓存的长度
//     *
//     * @param key 键
//     * @return
//     */
//    public Long lGetListSize(String key) {
//        try {
//            return stringRedisTemplate.opsForList().size(key);
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            return 0L;
//        }
//    }
//
//    /**
//     * 通过索引 获取list中的值
//     *
//     * @param key   键
//     * @param index 索引  index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
//     * @param clazz
//     * @return
//     */
//    public T lGetIndex(String key, long index, Class<T> clazz) {
//        try {
//            return JSON.parseObject(stringRedisTemplate.opsForList().index(key, index), clazz);
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            return null;
//        }
//    }
//
//    /**
//     * 将value放入缓存
//     *
//     * @param key   键
//     * @param value 值
//     * @return
//     */
//    public boolean lSet(String key, T value) {
//        try {
//            stringRedisTemplate.opsForList().rightPush(key, JSON.toJSONString(value));
//            return true;
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            return false;
//        }
//    }
//
//    /**
//     * 将value放入缓存
//     *
//     * @param key   键
//     * @param value 值
//     * @param time  时间(秒)
//     * @return
//     */
//    public boolean lSet(String key, T value, long time) {
//        try {
//            stringRedisTemplate.opsForList().rightPush(key, JSON.toJSONString(value));
//            if (time > 0) {
//                expire(key, time);
//            }
//            return true;
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            return false;
//        }
//    }
//
//    /**
//     * 将list放入缓存
//     *
//     * @param key   键
//     * @param value 值
//     * @return
//     */
//    public boolean lSet(String key, List<T> value) {
//        try {
//            stringRedisTemplate.opsForList().rightPushAll(key, value.stream().map(JSON::toJSONString).collect(Collectors.toList()));
//            return true;
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            return false;
//        }
//    }
//
//    /**
//     * 将list放入缓存
//     *
//     * @param key   键
//     * @param value 值
//     * @param time  时间(秒)
//     * @return
//     */
//    public boolean lSet(String key, List<T> value, long time) {
//        try {
//            stringRedisTemplate.opsForList().rightPushAll(key, value.stream().map(JSON::toJSONString).collect(Collectors.toList()));
//            if (time > 0) {
//                expire(key, time);
//            }
//            return true;
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            return false;
//        }
//    }
//
//    /**
//     * 根据索引修改list中的某条数据
//     *
//     * @param key   键
//     * @param index 索引
//     * @param value 值
//     * @return
//     */
//    public boolean lUpdateIndex(String key, long index, T value) {
//        try {
//            stringRedisTemplate.opsForList().set(key, index, JSON.toJSONString(value));
//            return true;
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            return false;
//        }
//    }
//
//    /**
//     * 移除N个值为value
//     *
//     * @param key   键
//     * @param count 移除多少个
//     * @param value 值
//     * @return 移除的个数
//     */
//    public Long lRemove(String key, long count, T value) {
//        try {
//            return stringRedisTemplate.opsForList().remove(key, count, JSON.toJSONString(value));
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            return 0L;
//        }
//    }

}