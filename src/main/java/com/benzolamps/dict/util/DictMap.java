package com.benzolamps.dict.util;

import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 操作Map的工具类
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-19 20:30:53
 */
public interface DictMap {

    /**
     * 将Map转换为Properties
     * @param map map
     * @return properties
     */
    static Properties convertToProperties(Map<?, ?> map) {
        if (null == map || map instanceof Properties) {
            return (Properties) map;
        }

        Properties properties = new Properties();

        Stack<KeyValuePairs<String, Map<?, ?>>> stack = new Stack<>();
        stack.push(new KeyValuePairs<>("", map));
        List<String> list = new ArrayList<>();
        while (!stack.empty()) {
            KeyValuePairs<String, Map<?, ?>> subMapPairs = stack.pop();
            Map<?, ?> subMap = subMapPairs.getValue();
            list.add(subMapPairs.getKey());
            for (Object key : subMap.keySet()) {
                Object value = subMap.get(key);
                if (value instanceof Map) {
                    stack.push(new KeyValuePairs<>(key.toString(), (Map<?, ?>) value));
                } else {
                    StringJoiner sj = new StringJoiner(".", "", "." + key.toString());
                    list.forEach(sj::add);
                    if (value != null) properties.setProperty(sj.toString(), value.toString());
                }
            }
            list.remove(list.size() - 1);
        }

        return properties;
    }

    /**
     * 通过YAML字符串构建一个Map
     * @param yaml YAML
     * @return map
     */
    @SuppressWarnings("rawtypes")
    static Map yamlMap(String yaml) {
        if (StringUtils.isEmpty(yaml)) {
            return Constant.EMPTY_MAP;
        }
        return Constant.YAML.loadAs(yaml, Map.class);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    static Map parse(Object... keyAndValues) {
        if (ObjectUtils.isEmpty(keyAndValues)) {
            return Constant.EMPTY_MAP;
        }

        Map map = new HashMap();
        for (int i = 0; i < keyAndValues.length; i++) {
            if (i < keyAndValues.length - 1) {
                map.put(keyAndValues[i], keyAndValues[++i]);
            } else {
                map.put(keyAndValues[i], null);
            }
        }
        return map;
    }
}
