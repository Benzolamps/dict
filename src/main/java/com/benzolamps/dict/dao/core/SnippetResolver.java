package com.benzolamps.dict.dao.core;

import com.benzolamps.dict.util.AddOnlyList;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Stream;

/**
 * 代码片段解析器, 用于筛选和排序
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-9-9 16:31:38
 */
public class SnippetResolver {

    private static final long serialVersionUID = 3374190442609095665L;

    /** 操作符代码片段 */
    public static class OperatorSnippet {

        private final String operator;

        /**
         * 构造器
         * @param operator 操作符
         */
        public OperatorSnippet(String operator) {
            this.operator = operator;
        }

        @Override
        public String toString() {
            return operator;
        }
    }

    /** 字段代码片段 */
    public static class FieldSnippet {
        private final String field;

        /**
         * 构造器
         * @param field 字段
         */
        public FieldSnippet(String field) {
            this.field = field;
        }

        @Override
        public String toString() {
            return field;
        }
    }

    /* 参数 */
    @JsonIgnore
    private List<Object> parameters;

    /* 代码片段 */
    private List<Object> snippets = new AddOnlyList<>();

    /* 代码片段 */
    @JsonIgnore
    private StringJoiner snippet;

    /**
     * 构建
     * @param alias 别名
     */
    public void build(String alias) {
        Assert.hasLength(alias, "alias不能为null或空");
        parameters = new ArrayList<>();
        snippet = new StringJoiner(" ");
        for (Object snippet : this.snippets) {
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

    protected void add(Object snippet) {
        snippets.add(snippet);
    }

    protected void add(int index, Object snippet) {
        snippets.add(index, snippet);
    }

    protected void addAll(SnippetResolver another) {
        snippets.addAll(another.snippets);
    }

    protected void addAll(int index, SnippetResolver snippetResolver) {
        snippets.addAll(index, snippetResolver.snippets);
    }

    protected boolean isEmpty() {
        return snippets.isEmpty();
    }

    protected int size() {
        return snippets.size();
    }

    protected Iterator<Object> iterator() {
        return snippets.iterator();
    }

    protected Stream<Object> stream() {
        return snippets.stream();
    }

    @Override
    public String toString() {
        return snippets.toString();
    }
}
