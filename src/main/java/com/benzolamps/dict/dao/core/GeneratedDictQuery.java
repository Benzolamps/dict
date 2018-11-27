package com.benzolamps.dict.dao.core;

import com.benzolamps.dict.bean.BaseEntity;
import com.benzolamps.dict.component.Alias;
import com.benzolamps.dict.util.DictObject;
import com.benzolamps.dict.util.DictString;
import lombok.Setter;
import org.springframework.core.ResolvableType;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    /* 类名 */
    private String className;

    /* 别名 */
    private String alias;

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
        initClassNameAndAlias();
    }

    /** 构造方法 */
    @SuppressWarnings("unchecked")
    protected GeneratedDictQuery() {
        ResolvableType resolvableType = ResolvableType.forClass(getClass());
        entityClass = (Class<B>) resolvableType.getSuperType().getGeneric().resolve();
        initClassNameAndAlias();
    }

    private void initClassNameAndAlias() {
        className = entityClass.getName();
        alias = entityClass.isAnnotationPresent(Alias.class) ?
            entityClass.getAnnotation(Alias.class).value() :
            DictString.toCamel(entityClass.getSimpleName());
    }

    @Override
    public void applyOrder(Order order) {
        if (!this.orders.contains(order)) {
            this.orders.add(order);
        }
    }

    @Override
    public void applySearch(Search search) {
        if (search != null && !StringUtils.isEmpty(search.getValue())) {
            String value = search.getValue().toString().replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_");
            filter.and(Filter.likeIgnoreCase(search.getField(), '%' + value + '%', '\\'));
        }
    }

    @Override
    public final TypedQuery<B> getTypedQuery() {
        Assert.notNull(entityManager, "entity manager不能为null");
        String jpql = select(alias);
        return DictJpa.createJpqlQuery(entityManager, jpql, entityClass, null, filter.getParameters().toArray());
    }

    @Override
    public final TypedQuery<? extends Number> getCountQuery() {
        Assert.notNull(entityManager, "entity manager不能为null");
        String jpql = select("count(1)");
        return DictJpa.createJpqlQuery(entityManager, jpql, Long.class, null, filter.getParameters().toArray());
    }

    private String select(String field) {
        Assert.hasText(field, "field不能为null或空");
        StringJoiner jpql = new StringJoiner(" ");
        filter.build(alias);
        applyOrder(Order.desc("id"));
        jpql.add("select").add(field).add("from").add(className).add("as").add(alias);
        jpql.add("where").add(filter.getSnippet());
        jpql.add("order by");
        jpql.add(String.join(", ", orders.stream().peek(order -> order.build(alias)).map(Order::getSnippet).collect(Collectors.toList())));
        return jpql.toString();
    }

    @Override
    public final Filter getFilter() {
        return filter;
    }

    protected final void applyRangeFilter(Search rangeSearch) {
        getFilter().and(Filter.betweenAnd(
            rangeSearch.getField(),
            Stream.of(rangeSearch.getValue().toString().split("[ \\s\\u00a0]*~[ \\s\\u00a0]*"))
                .map(str -> DictObject.ofObject(str, int.class))
                .toArray())
        );
    }
}
