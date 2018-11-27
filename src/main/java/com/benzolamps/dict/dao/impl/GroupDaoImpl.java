package com.benzolamps.dict.dao.impl;

import com.benzolamps.dict.bean.Group;
import com.benzolamps.dict.bean.Library;
import com.benzolamps.dict.bean.Student;
import com.benzolamps.dict.dao.base.GroupDao;
import com.benzolamps.dict.dao.base.StudentDao;
import com.benzolamps.dict.dao.core.*;
import com.benzolamps.dict.util.DictObject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

import static com.benzolamps.dict.util.Constant.SIMPLE_DATE_FORMAT;

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
            @SneakyThrows(ParseException.class)
            public void applySearch(Search search) {
                if ("studentNumber".equals(search.getField())) {
                    studentNumber = DictObject.ofObject(search.getValue(), int.class);
                } else if ("createDate".equals(search.getField())) {
                    String[] dates = search.getValue().toString().split(" ~ ");
                    String begin = dates[0];
                    String end = dates[1];
                    Date beginDate = SIMPLE_DATE_FORMAT.parse(begin);
                    Date endDate = SIMPLE_DATE_FORMAT.parse(end);
                    getFilter().and(Filter.betweenAnd("createDate", beginDate, endDate));
                } else if ("order".equals(search.getField())) {
                    switch (search.getValue().toString()) {
                        case "studentsCount asc": this.applyOrder(Order.asc("studentsOriented")); return;
                        case "studentsCount desc": this.applyOrder(Order.desc("studentsOriented")); return;
                        case "wordsCount asc": this.applyOrder(Order.asc("words")); return;
                        case "wordsCount desc": this.applyOrder(Order.desc("words")); return;
                        case "phrasesCount asc": this.applyOrder(Order.asc("phrases")); return;
                        case "phrasesCount desc": this.applyOrder(Order.desc("phrases")); return;
                        case "scoreCount asc": this.applyOrder(Order.asc("scoreCount")); return;
                        case "scoreCount desc": this.applyOrder(Order.desc("scoreCount"));
                    }
                } else if ("frequencyGenerated".equals(search.getField())) {
                    getFilter().and(Filter.eq("frequencyGenerated", DictObject.ofObject(search.getValue(), int.class) == 1));
                } else if ("studentsCount".equals(search.getField())) {
                    applyRangeFilter(new Search("studentsOrientedCount", search.getValue()));
                } else if ("wordsCount".equals(search.getField())) {
                    applyRangeFilter(search);
                } else if ("phrasesCount".equals(search.getField())) {
                    applyRangeFilter(search);
                } else if ("scoreCount".equals(search.getField())) {
                    applyRangeFilter(search);
                } else if (!"studentName".equals(search.getField())){
                    super.applySearch(search);
                }
            }

            @Override
            public void applySearches(Collection<Search> searches) {
                super.applySearches(searches);
                if (studentNumber != null) {
                    Student student = studentDao.findByNumber(studentNumber);
                    getFilter().and(Filter.memberOf("studentsOriented", student));
                    if (student != null) {
                        searches.add(new Search("studentName", student.getName()));
                        searches.add(new Search("student", student));
                    }
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

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Number> findMaxInfo(Library library) {
        String jpql =
            "select " +
            "max(groups.studentsOrientedCount) as maxStudentsCount, " +
            "max(groups.wordsCount) as maxWordsCount, " +
            "max(groups.phrasesCount) as maxPhrasesCount, " +
            "max(groups.scoreCount) as maxScoreCount " +
            "from Group as groups " +
            "where groups.library = :library";
        return (Map<String, Number>) DictJpa.createJpqlQuery(entityManager, jpql, Collections.singletonMap("library", library)).uniqueResult();
    }
}
