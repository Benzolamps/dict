package com.benzolamps.dict.dao.core;

import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 筛选条件
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-25 19:17:46
 */
@SuppressWarnings({"unused", "SpellCheckingInspection"})
public class Filter implements Serializable {

    private static final long serialVersionUID = -2317957195936033587L;

    /* 操作符代码片段 */
    private static class OperatorSnippet {

        private final String operator;

        OperatorSnippet(String operator) {
            this.operator = operator;
        }

        @Override
        public String toString() {
            return operator;
        }
    }

    /* 字段代码片段 */
    private static class FieldSnippet {
        private final String field;

        FieldSnippet(String field) {
            this.field = field;
        }

        @Override
        public String toString() {
            return field;
        }
    }

    /* 代码片段 */
    private List<Object> snippets;

    /* 参数 */
    private List<Object> parameters;

    /* 代码条件片段 */
    private StringJoiner snippet;

    /** 构造方法 */
    public Filter() {
        snippets = new ArrayList<>();
    }

    /* 添加一个片段 */
    private Filter addSnippet(Object obj) {
        snippets.add(obj);
        return this;
    }

    /**
     * 与
     * @param another another
     * @return Filter
     */
    public Filter and(Filter another) {
        Assert.notNull(another, "another不能为null");
        if (!another.snippets.isEmpty()) {
            if (snippets.isEmpty()) snippets.add(new OperatorSnippet("1 = 1"));
            snippets.add(new OperatorSnippet("and ("));
            snippets.addAll(another.snippets);
            snippets.add(new OperatorSnippet(")"));
        }
        return this;
    }

    /**
     * 或
     * @param another another
     * @return Filter
     */
    public Filter or(Filter another) {
        Assert.notNull(another, "another不能为null");
        if (!another.snippets.isEmpty()) {
            if (snippets.isEmpty()) snippets.add(new OperatorSnippet("1 = 1"));
            snippets.add(new OperatorSnippet("or ("));
            snippets.addAll(another.snippets);
            snippets.add(new OperatorSnippet(")"));
        }
        return this;
    }

    /**
     * 非
     * @return Filter
     */
    public Filter not() {
        if (snippets.isEmpty()) snippets.add(new OperatorSnippet("1 = 1"));
        snippets.add(0, new OperatorSnippet("not ("));
        snippets.add(new OperatorSnippet(")"));
        return this;
    }

    /**
     * 构建
     * @param alias 别名
     */
    @SuppressWarnings("deprecation")
    public void build(String alias) {
        Assert.hasLength(alias, "alias不能为null或空");
        if (snippets.isEmpty()) snippets.add(new OperatorSnippet("1 = 1"));
        parameters = new ArrayList<>();
        snippet = new StringJoiner(" ");
        for (Object snippet : snippets) {
            if (snippet instanceof OperatorSnippet) {
                this.snippet.add(snippet.toString());
            } else if (snippet instanceof FieldSnippet) {
                this.snippet.add(alias + "." + snippet.toString());
            } else {
                this.snippet.add("?" + parameters.size());
                parameters.add(snippet);
            }
        }
    }

    /**
     * 获取代码片段
     * @return 代码片段
     */
    public String getSnippet() {
        return snippet.toString();
    }

    /**
     * 获取参数集合
     * @return 参数集合
     */
    public List<Object> getParameters() {
        return parameters;
    }

    private static Filter binary(String field, String operator, Object value) {
        return new Filter()
            .addSnippet(new FieldSnippet(field))
            .addSnippet(new OperatorSnippet(operator))
            .addSnippet(value);
    }

    private static Filter binaryIgnoreCase(String field, String operator, Object value) {
        return new Filter()
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
        Assert.hasLength(field, "field不能为null或空");
        Assert.notNull(value, "value不能为null");
        return binary(field, "=", value);
    }

    /**
     * equals ignore case
     * @param field 字段
     * @param value 值
     * @return Filter
     */
    public static Filter eqic(String field, String value) {
        Assert.hasLength(field, "field不能为null或空");
        Assert.hasLength(value, "value不能为null或空");
        return binaryIgnoreCase(field, "=", value);
    }

    /**
     * not equals
     * @param field 字段
     * @param value 值
     * @return Filter
     */
    public static Filter neq(String field, Object value) {
        Assert.hasLength(field, "field不能为null或空");
        Assert.notNull(value, "value不能为null");
        return binary(field, "<>", value);
    }

    /**
     * not equals ignore case
     * @param field 字段
     * @param value 值
     * @return Filter
     */
    public static Filter neqic(String field, String value) {
        Assert.hasLength(field, "field不能为null或空");
        Assert.hasLength(value, "value不能为null或空");
        return binaryIgnoreCase(field, "<>", value);
    }

    /**
     * less than
     * @param field 字段
     * @param value 值
     * @return Filter
     */
    public static Filter lt(String field, Object value) {
        Assert.hasLength(field, "field不能为null或空");
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
        Assert.hasLength(field, "field不能为null或空");
        Assert.hasLength(value, "value不能为null或空");
        return binaryIgnoreCase(field, "<", value);
    }

    /**
     * less than or equals
     * @param field 字段
     * @param value 值
     * @return Filter
     */
    public static Filter le(String field, Object value) {
        Assert.hasLength(field, "field不能为null或空");
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
        Assert.hasLength(field, "field不能为null或空");
        Assert.hasLength(value, "value不能为null或空");
        return binaryIgnoreCase(field, "<=", value);
    }



    /**
     * great than
     * @param field 字段
     * @param value 值
     * @return Filter
     */
    public static Filter gt(String field, Object value) {
        Assert.hasLength(field, "field不能为null或空");
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
        Assert.hasLength(field, "field不能为null或空");
        Assert.hasLength(value, "value不能为null或空");
        return binaryIgnoreCase(field, ">", value);
    }

    /**
     * greater than or equals
     * @param field 字段
     * @param value 值
     * @return Filter
     */
    public static Filter ge(String field, Object value) {
        Assert.hasLength(field, "field不能为null或空");
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
        Assert.hasLength(field, "field不能为null或空");
        Assert.hasLength(value, "value不能为null或空");
        return binaryIgnoreCase(field, "<", value);
    }

    /**
     * like
     * @param field 字段
     * @param value 值
     * @return Filter
     */
    public static Filter like(String field, String value) {
        Assert.hasLength(field, "field不能为null或空");
        Assert.hasLength(value, "value不能为null或空");
        return binary(field, "like", value);
    }

    /**
     * like ignore case
     * @param field 字段
     * @param value 值
     * @return Filter
     */
    public static Filter likeIgnoreCase(String field, String value) {
        Assert.hasLength(field, "field不能为null或空");
        Assert.hasLength(value, "value不能为null或空");
        return binaryIgnoreCase(field, "like", value);
    }

    /**
     * not like
     * @param field 字段
     * @param value 值
     * @return Filter
     */
    public static Filter notLike(String field, String value) {
        Assert.hasLength(field, "field不能为null或空");
        Assert.hasLength(value, "value不能为null或空");
        return binary(field, "not like", value);
    }

    /**
     * not like ignore case
     * @param field 字段
     * @param value 值
     * @return Filter
     */
    public static Filter notLikeIgnoreCase(String field, String value) {
        Assert.hasLength(field, "field不能为null或空");
        Assert.hasLength(value, "value不能为null或空");
        return binaryIgnoreCase(field, "not like", value);
    }

    /**
     * in
     * @param field 字段
     * @param value 值
     * @return Filter
     */
    public static Filter in(String field, Collection value) {
        Assert.hasLength(field, "field不能为null或空");
        if (value == null) value = new HashSet<>();
        return binary(field, "in", value);
    }

    /**
     * in ignore case
     * @param field 字段
     * @param value 值
     * @return Filter
     */
    public static Filter inIgnoreCase(String field, Collection<String> value) {
        Assert.hasLength(field, "field不能为null或空");
        if (value == null) value = new HashSet<>();
        else value = value.stream().map(String::toLowerCase).collect(Collectors.toSet());
        return new Filter()
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
    public static Filter notIn(String field, Collection value) {
        Assert.hasLength(field, "field不能为null或空");
        if (value == null) value = new HashSet<>();
        return binary(field, "not in", value);
    }

    /**
     * not in ignore case
     * @param field 字段
     * @param value 值
     * @return Filter
     */
    public static Filter notInIgnoreCase(String field, Collection<String> value) {
        Assert.hasLength(field, "field不能为null或空");
        if (value == null) value = new HashSet<>();
        else value = value.stream().map(String::toLowerCase).collect(Collectors.toSet());
        return new Filter()
            .addSnippet(new OperatorSnippet("lower("))
            .addSnippet(new FieldSnippet(field))
            .addSnippet(new OperatorSnippet(") not in"))
            .addSnippet(value);
    }

    /**
     * between and
     * @param field 字段
     * @param left left
     * @param right right
     * @return Filter
     */
    public static Filter betweenAnd(String field, Object left, Object right) {
        Assert.hasLength(field, "field不能为null或空");
        Assert.notNull(left, "left不能为null");
        Assert.notNull(right, "right不能为null");
        return new Filter()
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
        Assert.hasLength(field, "field不能为null或空");
        Assert.hasLength(left, "left不能为null或空");
        Assert.hasLength(right, "right不能为null或空");
        return new Filter()
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
        Assert.hasLength(field, "field不能为null或空");
        return new Filter().addSnippet(new FieldSnippet(field)).addSnippet(new OperatorSnippet("is null"));
    }

    /**
     * is not null
     * @param field 字段
     * @return Filter
     */
    public static Filter isNotNull(String field) {
        Assert.hasLength(field, "field不能为null或空");
        return new Filter().addSnippet(new FieldSnippet(field)).addSnippet(new OperatorSnippet("is not null"));
    }
}
