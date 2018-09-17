package com.benzolamps.dict.dao.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * 页面
 * @param <B> 实体类型
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-25 19:51:52
 */
@SuppressWarnings("unused")
public class Page<B> implements Serializable {

    private static final long serialVersionUID = -1514961773054171834L;

    /** 内容 */
    @Getter
    private final List<B> content;

    /** 总数 */
    @Getter
    private final Integer total;

    /* pageable */
    @JsonIgnore
    private final Pageable pageable;

    /**
     * 构造器
     * @param content 内容
     * @param total 总数
     * @param pageable Pageable
     */
    public Page(List<B> content, Integer total, Pageable pageable) {
        this.content = content;
        this.total = total;
        this.pageable = pageable;
    }

    /**
     * 获取页码
     * @return 页码
     */
    public int getPageNumber() {
        return pageable.getPageNumber();
    }

    /**
     * 获取每页记录数
     * @return 每页记录数
     */
    public int getPageSize() {
        return pageable.getPageSize();
    }

    /**
     * 获取排序
     * @return 排序
     */
    public List<Order> getOrders() {
        return pageable.getOrders();
    }

    /**
     * 获取搜索
     * @return 搜索
     */
    public Set<Search> getSearches() {
        return pageable.getSearches();
    }

    /**
     * 获取总页数
     * @return 总页数
     */
    public Integer getTotalPages() {
        return (getTotal() - 1) / getPageSize() + 1;
    }
}
