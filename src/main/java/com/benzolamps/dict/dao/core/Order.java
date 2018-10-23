package com.benzolamps.dict.dao.core;

import com.benzolamps.dict.util.DictArray;
import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.util.Assert;

/**
 * 排序
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-25 19:44:14
 */
@SuppressWarnings("unused")
@NoArgsConstructor
@AllArgsConstructor
public class Order extends SnippetResolver {

    private static final long serialVersionUID = 2087247463448589053L;

    /** 排序方向 */
    public enum Direction {

        /** 升序 */
        ASC,

        /** 降序 */
        DESC
    }

    /** 排序字段 */
    @Getter
    @Setter
    @NotEmpty
    private String field;

    /** 排序方向 */
    @Getter
    @Setter
    private Direction direction;

    /**
     * 生成排序字段代码片段
     * @param field 字段
     */
    protected void applyField(String field) {
        Assert.hasText(field, "field不能为null或空");
        if ("0".equals(field)) {
            addSnippet(new OperatorSnippet(field));
        } else {
            addSnippet(new FieldSnippet(field));
        }
    }

    @Override
    public void build(String alias) {
        if (this.isEmpty()) {
            applyField(field);
            if (direction != null) {
                addSnippet(new OperatorSnippet(direction.name().toLowerCase()));
            }
        }
        super.build(alias);
    }

    /** 忽略大小写排序 */
    @NoArgsConstructor
    public static class IgnoreCaseOrder extends Order {

        private static final long serialVersionUID = -6810719711546840379L;

        /**
         * 构造器
         * @param field 字段
         * @param direction 排序方向
         */
        public IgnoreCaseOrder(String field, Direction direction) {
            super(field, direction);
        }

        @Override
        protected void applyField(String field) {
            addSnippet(new OperatorSnippet("lower("));
            super.applyField(field);
            addSnippet(new OperatorSnippet(")"));
        }
    }

    /** 个数排序 */
    @NoArgsConstructor
    public static class SizeOrder extends Order {

        private static final long serialVersionUID = -6810719711546840379L;

        /**
         * 构造器
         * @param field 字段
         * @param direction 排序方向
         */
        public SizeOrder(String field, Direction direction) {
            super(field, direction);
        }

        @Override
        protected void applyField(String field) {
            Assert.hasText("field", "field不能为null或空");
            super.applyField(field + ".size");
        }
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj) ||
            obj.getClass().equals(this.getClass()) &&
            ((Order) obj).direction.equals(this.direction) &&
            ((Order) obj).field.equals(this.field);
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

    /**
     * 忽略大小写降序实例
     * @param field 字段
     * @return Order
     */
    public static Order descIgnoreCase(String field) {
        return new IgnoreCaseOrder(field, Direction.DESC);
    }

    /**
     * 忽略大小写升序实例
     * @param field 字段
     * @return Order
     */
    public static Order ascIgnoreCase(String field) {
        return new IgnoreCaseOrder(field, Direction.ASC);
    }

    /**
     * 个数降序实例
     * @param field 字段
     * @return Order
     */
    public static Order descSize(String field) {
        return new SizeOrder(field, Direction.DESC);
    }

    /**
     * 个数升序实例
     * @param field 字段
     * @return Order
     */
    public static Order ascSize(String field) {
        return new SizeOrder(field, Direction.ASC);
    }

    /**
     * 转换成给定排序类型
     * @param orderClass 类型
     * @param <T> 类型
     * @return 对象
     */
    @SneakyThrows(ReflectiveOperationException.class)
    public <T extends Order> T convert(Class<T> orderClass) {
        Assert.notNull(orderClass, "order class不能为null");
        return orderClass.getConstructor(String.class, Direction.class).newInstance(field, direction);
    }

    /**
     * 转换成给定排序类型
     * @param orderClass 类型
     * @return 对象
     */
    @SneakyThrows(ReflectiveOperationException.class)
    public Order convertIf(Class<? extends Order> orderClass, String... fields) {
        Assert.notNull(orderClass, "order class不能为null");

        if (!DictArray.contains(fields, field)) {
            return this;
        }

        return orderClass.getConstructor(String.class, Direction.class).newInstance(field, direction);
    }


    /**
     * 替换排序方向生成新的order
     * @param direction 排序方向
     * @return 生成后的order
     */
    @SneakyThrows(ReflectiveOperationException.class)
    public Order reverse(Direction direction) {
        Assert.notNull(direction, "direction不能为null");
        return getClass().getConstructor(String.class, Direction.class).newInstance(field, direction);
    }
}
