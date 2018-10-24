package com.benzolamps.dict.dao.core;

import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * 筛选条件
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-25 19:17:46
 */
@SuppressWarnings({"unused", "SpellCheckingInspection"})
public class Filter extends SnippetResolver {

    private static final long serialVersionUID = -2317957195936033587L;

    /**
     * 与
     * @param another another
     * @return Filter
     */
    public Filter and(Filter another) {
        if (another != null && !another.isEmpty()) {
            if (!this.isEmpty()) {
                this.add(0, new OperatorSnippet("("));
                this.add(new OperatorSnippet(") and ("));
                this.addAll(another);
                this.add(new OperatorSnippet(")"));
            } else {
                this.addAll(another);
            }
        }
        return this;
    }

    /**
     * 或
     * @param another another
     * @return Filter
     */
    public Filter or(Filter another) {
        if (another != null && !another.isEmpty()) {
            if (!this.isEmpty()) {
                this.add(0, new OperatorSnippet("("));
                this.add(new OperatorSnippet(") or ("));
                this.addAll(another);
                this.add(new OperatorSnippet(")"));
            } else {
                this.addAll(another);
            }
        }
        return this;
    }

    /**
     * 非
     * @return Filter
     */
    public Filter not() {
        if (this.isEmpty()) this.add(new OperatorSnippet("1 = 0"));
        else {
            this.add(0, new OperatorSnippet("not ("));
            this.add(new OperatorSnippet(")"));
        }
        return this;
    }

    /**
     * 构建
     * @param alias 别名
     */
    @Override
    public void build(String alias) {
        if (this.isEmpty()) this.add(new OperatorSnippet("1 = 1"));
        super.build(alias);
    }

    private static Filter binary(String field, String operator, Object value) {
        return (Filter) new Filter()
            .addSnippet(new FieldSnippet(field))
            .addSnippet(new OperatorSnippet(operator))
            .addSnippet(value);
    }

    private static Filter binarySelf(String operator, Object value) {
        return (Filter) new Filter()
            .addSnippet(new AliasSnippet())
            .addSnippet(new OperatorSnippet(operator))
            .addSnippet(value);
    }

    private static Filter binaryIgnoreCase(String field, String operator, Object value) {
        return (Filter) new Filter()
            .addSnippet(new OperatorSnippet("lower("))
            .addSnippet(new FieldSnippet(field))
            .addSnippet(new OperatorSnippet(") " + operator + " lower("))
            .addSnippet(value)
            .addSnippet(new OperatorSnippet(")"));
    }

    /**
     * equals
     * @param field 字段
     * @param value 值
     * @return Filter
     */
    public static Filter eq(String field, Object value) {
        //Assert.hasText(field, "field不能为null或空");
        Assert.notNull(value, "value不能为null");
        if (StringUtils.isEmpty(field)) return binarySelf("=", value);
        return binary(field, "=", value);
    }

    /**
     * equals ignore case
     * @param field 字段
     * @param value 值
     * @return Filter
     */
    public static Filter eqic(String field, String value) {
        Assert.hasText(field, "field不能为null或空");
        Assert.notNull(value, "value不能为null");
        return binaryIgnoreCase(field, "=", value);
    }

    /**
     * not equals
     * @param field 字段
     * @param value 值
     * @return Filter
     */
    public static Filter neq(String field, Object value) {
        // Assert.hasText(field, "field不能为null或空");
        Assert.notNull(value, "value不能为null");
        if (StringUtils.isEmpty(field)) return binarySelf("<>", value);
        return binary(field, "<>", value);
    }

    /**
     * not equals ignore case
     * @param field 字段
     * @param value 值
     * @return Filter
     */
    public static Filter neqic(String field, String value) {
        Assert.hasText(field, "field不能为null或空");
        Assert.notNull(value, "value不能为null");
        return binaryIgnoreCase(field, "<>", value);
    }

    /**
     * less than
     * @param field 字段
     * @param value 值
     * @return Filter
     */
    public static Filter lt(String field, Object value) {
        Assert.hasText(field, "field不能为null或空");
        Assert.notNull(value, "value不能为null");
        return binary(field, "<", value);
    }

    /**
     * less than ignore case
     * @param field 字段
     * @param value 值
     * @return Filter
     */
    public static Filter ltic(String field, String value) {
        Assert.hasText(field, "field不能为null或空");
        Assert.notNull(value, "value不能为null");
        return binaryIgnoreCase(field, "<", value);
    }

    /**
     * less than or equals
     * @param field 字段
     * @param value 值
     * @return Filter
     */
    public static Filter le(String field, Object value) {
        Assert.hasText(field, "field不能为null或空");
        Assert.notNull(value, "value不能为null");
        return binary(field, "<=", value);
    }

    /**
     * less than or equals ignore case
     * @param field 字段
     * @param value 值
     * @return Filter
     */
    public static Filter leic(String field, String value) {
        Assert.hasText(field, "field不能为null或空");
        Assert.notNull(value, "value不能为null");
        return binaryIgnoreCase(field, "<=", value);
    }

    /**
     * great than
     * @param field 字段
     * @param value 值
     * @return Filter
     */
    public static Filter gt(String field, Object value) {
        Assert.hasText(field, "field不能为null或空");
        Assert.notNull(value, "value不能为null");
        return binary(field, ">", value);
    }

    /**
     * great than ignore case
     * @param field 字段
     * @param value 值
     * @return Filter
     */
    public static Filter gtic(String field, String value) {
        Assert.hasText(field, "field不能为null或空");
        Assert.notNull(value, "value不能为null");
        return binaryIgnoreCase(field, ">", value);
    }

    /**
     * greater than or equals
     * @param field 字段
     * @param value 值
     * @return Filter
     */
    public static Filter ge(String field, Object value) {
        Assert.hasText(field, "field不能为null或空");
        Assert.notNull(value, "value不能为null");
        return binary(field, ">=", value);
    }

    /**
     * greater than or equals ignore case
     * @param field 字段
     * @param value 值
     * @return Filter
     */
    public static Filter geic(String field, String value) {
        Assert.hasText(field, "field不能为null或空");
        Assert.notNull(value, "value不能为null");
        return binaryIgnoreCase(field, "<", value);
    }

    /**
     * like
     * @param field 字段
     * @param value 值
     * @param escape 转义
     * @return Filter
     */
    public static Filter like(String field, String value, Character escape) {
        Assert.hasText(field, "field不能为null或空");
        Assert.notNull(value, "value不能为null");
        Filter filter = binary(field, "like", value);
        if (escape != null) {
            filter.addSnippet(new OperatorSnippet("escape")).addSnippet(escape);
        }
        return filter;
    }

    /**
     * like ignore case
     * @param field 字段
     * @param value 值
     * @param escape 转义
     * @return Filter
     */
    public static Filter likeIgnoreCase(String field, String value, Character escape) {
        Assert.hasText(field, "field不能为null或空");
        Assert.notNull(value, "value不能为null");
        Filter filter = binaryIgnoreCase(field, "like", value);
        if (escape != null) {
            filter.addSnippet(new OperatorSnippet("escape")).addSnippet(escape);
        }
        return filter;
    }

    /**
     * not like
     * @param field 字段
     * @param value 值
     * @param escape 转义
     * @return Filter
     */
    public static Filter notLike(String field, String value, Character escape) {
        Assert.hasText(field, "field不能为null或空");
        Assert.notNull(value, "value不能为null");
        Filter filter = binary(field, "not like", value);
        if (escape != null) {
            filter.addSnippet(new OperatorSnippet("escape")).addSnippet(escape);
        }
        return filter;
    }

    /**
     * not like ignore case
     * @param field 字段
     * @param value 值
     * @param escape 转义
     * @return Filter
     */
    public static Filter notLikeIgnoreCase(String field, String value, Character escape) {
        Assert.hasText(field, "field不能为null或空");
        Assert.notNull(value, "value不能为null");
        Filter filter = binaryIgnoreCase(field, "not like", value);
        if (escape != null) {
            filter.addSnippet(new OperatorSnippet("escape")).addSnippet(escape);
        }
        return filter;
    }

    /**
     * in
     * @param field 字段
     * @param value 值
     * @return Filter
     */
    public static Filter in(String field, Collection<?> value) {
        // Assert.hasText(field, "field不能为null或空");
        if (CollectionUtils.isEmpty(value)) return new Filter().not();
        if (StringUtils.isEmpty(field)) return binarySelf("in", value);
        return binary(field, "in", value);
    }

    /**
     * in ignore case
     * @param field 字段
     * @param value 值
     * @return Filter
     */
    public static Filter inIgnoreCase(String field, Collection<String> value) {
        Assert.hasText(field, "field不能为null或空");
        if (CollectionUtils.isEmpty(value)) return new Filter().not();
        else value = value.stream().map(String::toLowerCase).collect(Collectors.toSet());
        return (Filter) new Filter()
            .addSnippet(new OperatorSnippet("lower("))
            .addSnippet(new FieldSnippet(field))
            .addSnippet(new OperatorSnippet(") in"))
            .addSnippet(value);
    }

    /**
     * not in
     * @param field 字段
     * @param value 值
     * @return Filter
     */
    public static Filter notIn(String field, Collection<?> value) {
        // Assert.hasText(field, "field不能为null或空");
        if (CollectionUtils.isEmpty(value)) return new Filter();
        if (StringUtils.isEmpty(field)) return binarySelf("not in", value);
        return binary(field, "not in", value);
    }

    /**
     * not in ignore case
     * @param field 字段
     * @param value 值
     * @return Filter
     */
    public static Filter notInIgnoreCase(String field, Collection<String> value) {
        Assert.hasText(field, "field不能为null或空");
        if (CollectionUtils.isEmpty(value)) return new Filter();
        value = value.stream().map(String::toLowerCase).collect(Collectors.toSet());
        return (Filter) new Filter()
            .addSnippet(new OperatorSnippet("lower("))
            .addSnippet(new FieldSnippet(field))
            .addSnippet(new OperatorSnippet(") not in"))
            .addSnippet(value);
    }

    /**
     * member of
     * @param field 字段
     * @param value 值
     * @return Filter
     */
    public static Filter memberOf(String field, Object value) {
        Assert.hasText(field, "field不能为null或空");
        if (value == null) return new Filter().not();
        return (Filter) new Filter()
            .addSnippet(value)
            .addSnippet(new OperatorSnippet("in elements("))
            .addSnippet(new FieldSnippet(field))
            .addSnippet(new OperatorSnippet(")"));
    }

    /**
     * not member of
     * @param field 字段
     * @param value 值
     * @return Filter
     */
    public static Filter notMemberOf(String field, Object value) {
        Assert.hasText(field, "field不能为null或空");
        if (value == null) return new Filter();
        return (Filter) new Filter()
            .addSnippet(value)
            .addSnippet(new OperatorSnippet("not in elements("))
            .addSnippet(new FieldSnippet(field))
            .addSnippet(new OperatorSnippet(")"));
    }

    /**
     * between and
     * @param field 字段
     * @param left left
     * @param right right
     * @return Filter
     */
    public static Filter betweenAnd(String field, Object left, Object right) {
        Assert.hasText(field, "field不能为null或空");
        Assert.notNull(left, "left不能为null");
        Assert.notNull(right, "right不能为null");
        return (Filter) new Filter()
            .addSnippet(new FieldSnippet(field))
            .addSnippet(new OperatorSnippet("between"))
            .addSnippet(left)
            .addSnippet(new OperatorSnippet("and"))
            .addSnippet(right);
    }

    /**
     * between and ignore case
     * @param field 字段
     * @param left left
     * @param right right
     * @return Filter
     */
    public static Filter betweenAndIgnoreCase(String field, String left, String right) {
        Assert.hasText(field, "field不能为null或空");
        Assert.notNull(left, "left不能为null");
        Assert.notNull(right, "right不能为null");
        return (Filter) new Filter()
            .addSnippet(new OperatorSnippet("lower("))
            .addSnippet(new FieldSnippet(field))
            .addSnippet(new OperatorSnippet(") between lower("))
            .addSnippet(left)
            .addSnippet(new OperatorSnippet(") and lower("))
            .addSnippet(right)
            .addSnippet(new OperatorSnippet(")"));
    }

    /**
     * is null
     * @param field 字段
     * @return Filter
     */
    public static Filter isNull(String field) {
        // Assert.hasText(field, "field不能为null或空");
        return (Filter) new Filter()
            .addSnippet(StringUtils.isEmpty(field) ? new AliasSnippet() : new FieldSnippet(field))
            .addSnippet(new OperatorSnippet("is null"));
    }

    /**
     * is not null
     * @param field 字段
     * @return Filter
     */
    public static Filter isNotNull(String field) {
        // Assert.hasText(field, "field不能为null或空");
        return (Filter) new Filter()
            .addSnippet(StringUtils.isEmpty(field) ? new AliasSnippet() : new FieldSnippet(field))
            .addSnippet(new OperatorSnippet("is not null"));
    }

    /**
     * is empty
     * @param field 字段
     * @return Filter
     */
    public static Filter isEmpty(String field) {
        Assert.hasText(field, "field不能为null或空");
        return (Filter) new Filter().addSnippet(new FieldSnippet(field)).addSnippet(new OperatorSnippet("is empty"));
    }

    /**
     * is not empty
     * @param field 字段
     * @return Filter
     */
    public static Filter isNotEmpty(String field) {
        Assert.hasText(field, "field不能为null或空");
        return (Filter) new Filter().addSnippet(new FieldSnippet(field)).addSnippet(new OperatorSnippet("is not empty"));
    }
}
