package com.benzolamps.dict.dao.impl;

import com.benzolamps.dict.bean.Group;
import com.benzolamps.dict.dao.base.GroupDao;
import com.benzolamps.dict.dao.core.DictQuery;
import com.benzolamps.dict.dao.core.GeneratedDictQuery;
import com.benzolamps.dict.dao.core.Order;
import com.benzolamps.dict.dao.core.Page;
import com.benzolamps.dict.dao.core.Pageable;
import org.springframework.stereotype.Repository;

/**
 * 单词短语分组Dao接口实现类
 * @author Benzolamps
 * @version 2.1.4
 * @datetime 2018-9-21 22:08:37
 */
@Repository("groupDao")
public class GroupDaoImpl extends BaseDaoImpl<Group> implements GroupDao {

    @Override
    public Page<Group> findPage(Pageable pageable) {
        DictQuery<Group> dictQuery = new GeneratedDictQuery<Group>() {
            @Override
            public void applyOrder(Order order) {
                order = order.convertIf(Order.SizeOrder.class, "studentsOriented", "studentsScored", "words", "phrases");
                super.applyOrder(order);
            }
        };
        return super.findPage(dictQuery, pageable);
    }
}
