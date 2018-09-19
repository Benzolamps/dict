package com.benzolamps.dict.dao.impl;

import com.benzolamps.dict.bean.Clazz;
import com.benzolamps.dict.dao.base.ClazzDao;
import com.benzolamps.dict.dao.core.*;
import org.springframework.stereotype.Repository;

/**
 * 班级Dao接口实现类
 * @author Benzolamps
 * @version 2.1.4
 * @datetime 2018-9-19 21:59:45
 */
@Repository("clazzDao")
public class ClazzDaoImpl extends BaseDaoImpl<Clazz> implements ClazzDao {

    @Override
    public Page<Clazz> findPage(Pageable pageable) {
        DictQuery<Clazz> dictQuery = new GeneratedDictQuery<Clazz>(Clazz.class) {
            @Override
            public void applyOrder(Order order) {
                if (order.getField().equals("studentsCount")) {
                    order = new Order("students.size", order.getDirection());
                }
                super.applyOrder(order);
            }
        };
        return super.findPage(dictQuery, pageable);
    }
}
