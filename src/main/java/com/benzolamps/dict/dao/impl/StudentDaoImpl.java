package com.benzolamps.dict.dao.impl;

import com.benzolamps.dict.bean.Clazz;
import com.benzolamps.dict.bean.Student;
import com.benzolamps.dict.dao.base.ClazzDao;
import com.benzolamps.dict.dao.base.StudentDao;
import com.benzolamps.dict.dao.core.*;
import com.benzolamps.dict.util.DictObject;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * 学生Dao接口实现类
 * @author Benzolamps
 * @version 2.1.4
 * @datetime 2018-9-19 23:09:29
 */
@Repository("studentDao")
public class StudentDaoImpl extends BaseDaoImpl<Student> implements StudentDao {

    @Resource
    private ClazzDao clazzDao;

    @Override
    public Page<Student> findPage(Pageable pageable) {
        DictQuery<Student> dictQuery = new GeneratedDictQuery<Student>(Student.class) {
            @Override
            public void applySearch(Search search) {
                if (search.getField().equals("clazz")) {
                    Clazz clazz = clazzDao.find(DictObject.ofObject(search.getValue(), Integer.class));
                    getFilter().and(Filter.eq("clazz", clazz));
                } else {
                    super.applySearch(search);
                }
            }

            @SuppressWarnings("IfCanBeSwitch")
            @Override
            public void applyOrder(Order order) {
                if (order.getField().equals("clazz")) {
                    order = new Order("clazz.name", order.getDirection());
                } else if (order.getField().equals("masteredWordsCount")) {
                    order = new Order("masteredWords.size", order.getDirection());
                } else if (order.getField().equals("masteredPhrasesCount")) {
                    order = new Order("masteredPhrases.size", order.getDirection());
                }
                super.applyOrder(order);
            }
        };
        return super.findPage(dictQuery, pageable);
    }
}
