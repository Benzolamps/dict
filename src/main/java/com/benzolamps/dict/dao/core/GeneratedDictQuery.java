package com.benzolamps.dict.dao.core;

import com.benzolamps.dict.bean.BaseEntity;
import com.benzolamps.dict.util.DictString;
import lombok.Setter;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 生成好的自定义查询
 * @param <B> 实体类型
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-25 19:40:39
 */
public class GeneratedDictQuery<B extends BaseEntity> implements DictQuery<B> {

    /* 实体类型 */
    private final Class<B> entityClass;

    /* 排序 */
    private List<Order> orders = new ArrayList<>();

    /* 筛选 */
    private final Filter filter = new Filter();

    @Setter
    private EntityManager entityManager;

    /** 构造方法 */
    @SuppressWarnings("unchecked")
    public GeneratedDictQuery(Class<B> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public void applyOrder(Order order) {
        this.orders.add(order);
    }

    @Override
    public void applySearch(Search search) {
        if (search != null) {
            filter.and(Filter.likeIgnoreCase(search.getField(), '%' + String.valueOf(search.getValue()) + '%'));
        }
    }

    @Override
    public TypedQuery<B> getTypedQuery() {
        Assert.notNull(entityManager, "entity manager不能为空");
        String classSimpleName = entityClass.getSimpleName();
        String alias = DictString.toCamel(classSimpleName);
        String jpql = select(alias);
        return DictJpa.createJpqlQuery(entityManager, jpql, entityClass, null, filter.getParameters().toArray());
    }

    @Override
    public TypedQuery<Long> getCountQuery() {
        Assert.notNull(entityManager, "entity manager不能为空");
        String jpql = select("count(1)");
        return DictJpa.createJpqlQuery(entityManager, jpql, Long.class, null, filter.getParameters().toArray());
    }

    private String select(String field) {
        Assert.hasLength(field, "field不能为空或null");
        String className = entityClass.getName();
        String alias = DictString.toCamel(entityClass.getSimpleName());
        StringJoiner jpql = new StringJoiner(" ");
        filter.build(alias);
        Order defaultOrder = Order.desc("id");
        if (!orders.contains(defaultOrder)) orders.add(defaultOrder);
        orders.forEach(order -> order.build(alias));
        jpql.add("select").add(field).add("from").add(className).add("as").add(alias);
        jpql.add("where").add(filter.getSnippet());
        jpql.add("order by");
        jpql.add(String.join(", ", orders.stream().map(Order::getSnippet).collect(Collectors.toList())));
        return jpql.toString();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }
}
