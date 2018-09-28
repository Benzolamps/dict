package com.benzolamps.dict.util;

import org.springframework.util.Assert;
import java.util.Map;

/**
 * 自定义键值对
 * @param <K> key
 * @param <V> value
 * @author Benzolamps
 * @since 1.1.2
 * @see Map.Entry
 * @datetime 2018年6月17日12:21:25
 */
@SuppressWarnings("unused")
public class KeyValuePairs<K, V> implements Map.Entry<K, V> {

    /* 键 */
    private K key;

    /* 值 */
    private V value;

    /**
     * 初始化键和值
     * @param key 键
     * @param value 值
     */
    public KeyValuePairs(K key, V value) {
        this.key = key;
        setValue(value);
    }

    /**
     * 初始化键和值
     * @param entry entry
     */
    public KeyValuePairs(Map.Entry<K, V> entry) {
        Assert.notNull(entry, "entry不能为null");
        this.key = entry.getKey();
        setValue(entry.getValue());
    }

    /**
     * 初始化键和值
     * @param map map
     * @param key 键
     */
    public KeyValuePairs(Map<K, V> map, K key) {
        Assert.notNull(map, "map不能为空");
        this.key = key;
        setValue(map.getOrDefault(key, null));
    }

    @Override
    public K getKey() {
        return key;
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public V setValue(V value) {
        try {
            return getValue();
        } finally {
            this.value = value;
        }
    }

    @Override
    public String toString() {
        return key + ": " + value;
    }
}
