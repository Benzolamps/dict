package com.benzolamps.dict.dao.impl;

import com.benzolamps.dict.bean.Word;
import com.benzolamps.dict.bean.WordClazz;
import com.benzolamps.dict.dao.base.WordClazzDao;
import com.benzolamps.dict.dao.base.WordDao;
import com.benzolamps.dict.dao.core.*;
import com.benzolamps.dict.util.DictLambda;
import com.benzolamps.dict.util.DictObject;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 单词Dao接口实现类
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-1 22:13:15
 */
@SuppressWarnings("unchecked")
@Repository("wordDao")
public class WordDaoImpl extends BaseElementDaoImpl<Word> implements WordDao {

    @Resource
    private WordClazzDao wordClazzDao;

    @Override
    public Page<Word> findPage(Pageable pageable) {
        DictQuery<Word> dictQuery = new DictQuery<Word>() {
            private EntityManager entityManager;

            private CriteriaBuilder criteriaBuilder;

            private List<DictLambda.Action3<CriteriaQuery, Root, AtomicReference<Predicate>>> consumers = new ArrayList<>();

            @Override
            public void applyOrder(Order order) {
                consumers.add((query, root, restriction) -> query.orderBy(
                    order.getDirection().equals(Order.Direction.DESC) ?
                        criteriaBuilder.desc(criteriaBuilder.lower(root.get(order.getField()))) :
                        criteriaBuilder.asc(criteriaBuilder.lower(root.get(order.getField()))))
                );
            }

            @Override
            public void applySearch(Search search) {
                consumers.add((query, root, restriction) -> {
                    if (search.getField().equals("clazzes")) {
                        WordClazz wordClazz = wordClazzDao.find(DictObject.ofObject(search.getValue(), Integer.class));
                        Predicate r = criteriaBuilder.isMember(wordClazz, root.get("clazzes"));
                        restriction.set(criteriaBuilder.and(restriction.get(), r));
                    } else {
                        String field = search.getField();
                        String value = "%" + search.getValue() + "%";
                        Predicate r = criteriaBuilder.like(criteriaBuilder.lower(root.get(field)), value.toLowerCase());
                        restriction.set(criteriaBuilder.and(restriction.get(), r));
                    }
                });

            }

            @Override
            public TypedQuery<Word> getTypedQuery() {
                CriteriaQuery<Word> criteriaQuery = criteriaBuilder.createQuery(Word.class);
                Root<Word> root = criteriaQuery.from(Word.class);
                AtomicReference<Predicate> restriction = new AtomicReference<>(criteriaBuilder.conjunction());
                consumers.forEach(consumer -> consumer.tryExecute(criteriaQuery, root, restriction));
                criteriaQuery.where(restriction.get());
                criteriaQuery.select(root);
                return entityManager.createQuery(criteriaQuery);
            }

            @Override
            public TypedQuery<Long> getCountQuery() {
                CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
                Root<Word> root = criteriaQuery.from(Word.class);
                AtomicReference<Predicate> restriction = new AtomicReference<>(criteriaBuilder.conjunction());
                consumers.forEach(consumer -> consumer.tryExecute(criteriaQuery, root, restriction));
                criteriaQuery.where(restriction.get());
                criteriaQuery.select(criteriaBuilder.count(root));
                return entityManager.createQuery(criteriaQuery);

            }

            @Override
            public void setEntityManager(EntityManager entityManager) {
                this.entityManager = entityManager;
                this.criteriaBuilder = this.entityManager.getCriteriaBuilder();
            }
        };
        return super.findPage(dictQuery, pageable);
    }
}
