package com.benzolamps.dict.util;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

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
    static Properties convertToProperties(Map map) {
        if (null == map || map instanceof Properties) {
            return (Properties) map;
        }

        Properties properties = new Properties();

        Stack<KeyValuePairs<String, Map>> stack = new Stack<>();
        stack.push(new KeyValuePairs<>("", map));
        List<String> list = new ArrayList<>();
        while (!stack.empty()) {
            KeyValuePairs<String, Map> submapPairs = stack.pop();
            Map submap = submapPairs.getValue();
            list.add(submapPairs.getKey());
            for (Object key : submap.keySet()) {
                Object value = submap.get(key);
                if (value instanceof Map) {
                    stack.push(new KeyValuePairs<>(key.toString(), (Map) value));
                } else {
                    StringJoiner sj = new StringJoiner(".", "", "." + key.toString());
                    list.forEach(sj::add);
                    properties.setProperty(sj.toString(), value.toString());
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
    static Map yamlMap(String yaml) {
        if (StringUtils.isEmpty(yaml)) {
            return Constant.EMPTY_MAP;
        }
        return Constant.YAML.loadAs(yaml, Map.class);
    }
    
}
