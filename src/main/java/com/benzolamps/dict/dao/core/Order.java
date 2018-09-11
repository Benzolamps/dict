package com.benzolamps.dict.dao.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
        Assert.hasLength(field, "field不能为null或空");
        if (field.equals("0")) {
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
     * 降序实例
     * @param field 字段
     * @return Order
     */
    public static Order descIgnoreCase(String field) {
        return new IgnoreCaseOrder(field, Direction.DESC);
    }

    /**
     * 升序实例
     * @param field 字段
     * @return Order
     */
    public static Order ascIgnoreCase(String field) {
        return new IgnoreCaseOrder(field, Direction.ASC);
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj) ||
            obj.getClass().equals(this.getClass()) &&
            ((Order) obj).direction.equals(this.direction) &&
            ((Order) obj).field.equals(this.field);
    }
}
