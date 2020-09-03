package com.saim.demo.websocket.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * description: IMRedisUtil
 * date: 2020/7/28 15:00
 *
 * @author jh
 */
@Component
public class ImRedisUtil {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    //**************************************** list ***************************************************************

    /**
     * 获取 list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束（0 到 -1 代表所有值）
     * @return
     */
    public List<Object> lGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 查询list 全部缓存
     *
     * @param key 键
     * @return
     */
    public List<Object> lGetAll(String key) {
        try {
            return redisTemplate.opsForList().range(key, 0, -1);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据索引 index 获取键为 key 的 list 中的元素
     *
     * @param key   键
     * @param index 索引
     *              当 index >= 0 时 {0:表头, 1:第二个元素}
     *              当 index < 0 时 {-1:表尾, -2:倒数第二个元素}
     * @return 值
     */
    public Object lGetIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将 values 插入键为 key 的 list 中
     *
     * @param key    键
     * @param values 值
     * @return true / false
     */
    public boolean lSetList(String key, List<Object> values) {
        try {
            redisTemplate.opsForList().rightPushAll(key, values);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * list 覆盖
     *
     * @param key    键
     * @param values 值
     * @return true / false
     */
    public boolean lCover(String key, List<Object> values) {
        try {
            redisTemplate.delete(key);
            redisTemplate.opsForList().rightPushAll(key, values);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将值 value 插入键为 key 的 list 中，如果 list 不存在则创建空 list
     *
     * @param key   键
     * @param value 值
     * @return true / false
     */
    public boolean lSet(String key, Object value) {
        try {
            redisTemplate.opsForList().leftPush(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将值 value 插入键为 key  替换
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return true / false
     */
    public boolean lSet(String key, long index, Object value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取 list缓存的长度
     *
     * @param key 键
     * @return 长度
     */
    public long lGetListSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 删除缓存
     *
     * @param key 键（一个或者多个）
     * @SuppressWarnings("unchecked") 忽略类型转换警告
     */
    @SuppressWarnings("unchecked")
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
//                传入一个 Collection<String> 集合
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }
}
