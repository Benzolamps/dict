package com.benzolamps.dict.dao.core;

import com.benzolamps.dict.util.AddOnlyList;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.experimental.Delegate;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * 代码片段解析器, 用于筛选和排序
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-9-9 16:31:38
 */
@SuppressWarnings("unused")
public class SnippetResolver implements Serializable {

    private static final long serialVersionUID = 3374190442609095665L;

    /** 操作符代码片段 */
    @AllArgsConstructor
    protected static final class OperatorSnippet {

        private final String operator;

        @Override
        public String toString() {
            return operator;
        }
    }

    /** 字段代码片段 */
    @AllArgsConstructor
    protected static final class FieldSnippet {

        private final String field;

        @Override
        public String toString() {
            return field;
        }
    }

    /** 别名代码片段 */
    protected static final class AliasSnippet {
    }

    /* 参数 */
    @JsonIgnore
    private List<Object> parameters;

    /* 代码片段 */
    @Delegate(types = List.class)
    private List<Object> snippets = new AddOnlyList<>();

    /* 代码片段 */
    @JsonIgnore
    private transient StringJoiner snippet;

    /**
     * 构建
     * @param alias 别名
     */
    public void build(String alias) {
        Assert.hasText(alias, "alias不能为null或空");
        parameters = new ArrayList<>();
        snippet = new StringJoiner(" ");
        for (Object snippet : this.snippets) {
            if (snippet instanceof OperatorSnippet) {
                this.snippet.add(snippet.toString());
            } else if (snippet instanceof FieldSnippet) {
                this.snippet.add(alias + "." + snippet.toString());
            } else if (snippet instanceof AliasSnippet) {
                this.snippet.add(alias);
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
        return snippet != null ? snippet.toString() : null;
    }

    /**
     * 获取参数集合
     * @return 参数集合
     */
    public List<Object> getParameters() {
        return parameters;
    }

    /**
     * 添加代码片段
     * @param snippet 代码片段
     * @return this
     */
    public SnippetResolver addSnippet(Object snippet) {
        this.add(snippet);
        return this;
    }

    /**
     * 两个代码片段合并
     * @param another 另一个
     */
    public void addAll(SnippetResolver another) {
        this.addAll(another.snippets);
    }

    @Override
    public String toString() {
        return snippets.toString();
    }
}
