package com.benzolamps.dict.dao.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.io.Serializable;

/**
 * 排序
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-25 19:44:14
 */
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Order implements Serializable {

    private static final long serialVersionUID = 2087247463448589053L;

    /** 排序方向 */
    public enum Direction {

        /** 升序 */
        ASC,

        /** 降序 */
        DESC
    }

    /** 排序字段 */
    @NonNull
    private String field;

    /** 排序方向 */
    @NonNull
    private Direction direction;

    /** 排序代码片段 */
    @JsonIgnore
    private String snippet;

    /**
     * 构建
     * @param alias 别名
     */
    public void build(String alias) {
        snippet = alias + direction == null ? "" : (" " + direction.name().toLowerCase());
    }

    /**
     * 降序实例
     * @param field 字段
     * @return Order
     */
    public static Order desc(String field) {
        return new Order(field, Direction.DESC);
    }

    /**
     * 升序实例
     * @param field 字段
     * @return Order
     */
    public static Order asc(String field) {
        return new Order(field, Direction.ASC);
    }
}
