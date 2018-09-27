package com.benzolamps.dict.service.impl;

import com.benzolamps.dict.bean.Clazz;
import com.benzolamps.dict.service.base.ClazzService;
import com.benzolamps.dict.service.base.StudentService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collection;

/**
 * 班级Service接口实现类
 * @author Benzolamps
 * @version 2.1.4
 * @datetime 2018-9-19 22:01:23
 */
@Service("clazzService")
public class ClazzServiceImpl extends BaseServiceImpl<Clazz> implements ClazzService {

    @Resource
    private StudentService studentService;

    @Override
    public void remove(Collection<Clazz> clazzes) {
        /* 删除班级前先删除班级中的学生 */
        for (Clazz clazz : clazzes) {
            if (!CollectionUtils.isEmpty(clazz.getStudents())) {
                studentService.remove(clazz.getStudents());
            }
        }
        super.remove(clazzes);
    }
}
