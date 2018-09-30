package com.benzolamps.dict.dao.impl;

import com.benzolamps.dict.bean.Group;
import com.benzolamps.dict.bean.Phrase;
import com.benzolamps.dict.bean.Student;
import com.benzolamps.dict.bean.Word;
import com.benzolamps.dict.dao.base.GroupDao;
import com.benzolamps.dict.dao.core.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import javax.persistence.Query;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 单词短语分组Dao接口实现类
 * @author Benzolamps
 * @version 2.1.4
 * @datetime 2018-9-21 22:08:37
 */
@Repository("groupDao")
@Slf4j
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
