package com.benzolamps.dict.dao.impl;

import com.benzolamps.dict.bean.Group;
import com.benzolamps.dict.bean.Student;
import com.benzolamps.dict.dao.base.GroupDao;
import com.benzolamps.dict.dao.base.StudentDao;
import com.benzolamps.dict.dao.core.*;
import com.benzolamps.dict.util.DictObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Collection;

/**
 * 单词短语分组Dao接口实现类
 * @author Benzolamps
 * @version 2.1.4
 * @datetime 2018-9-21 22:08:37
 */
@Repository("groupDao")
@Slf4j
public class GroupDaoImpl extends BaseDaoImpl<Group> implements GroupDao {

    @Resource
    private StudentDao studentDao;

    @Override
    public Page<Group> findPage(Pageable pageable) {
        DictQuery<Group> dictQuery = new GeneratedDictQuery<Group>() {

            private Integer studentNumber;

            @Override
            public void applySearch(Search search) {
                if ("studentNumber".equals(search.getField())) {
                    studentNumber = DictObject.ofObject(search.getValue(), int.class);
                    return;
                } else if  ("studentName".equals(search.getField())) {
                    return;
                }
                super.applySearch(search);
            }

            @Override
            public void applySearches(Collection<Search> searches) {
                super.applySearches(searches);
                if (studentNumber != null) {
                    Student student = studentDao.findByNumber(studentNumber);
                    getFilter().and(Filter.memberOf("studentsOriented", student));
                    searches.add(new Search("studentName", student.getName()));
                    searches.add(new Search("student", student));
                }
            }

            @Override
            public void applyOrder(Order order) {
                order = order.convertIf(Order.SizeOrder.class, "studentsOriented", "studentsScored", "words", "phrases");
                super.applyOrder(order);
            }
        };
        return super.findPage(dictQuery, pageable);
    }
}
