package com.benzolamps.dict.service.base;

import com.benzolamps.dict.bean.Clazz;
import com.benzolamps.dict.controller.vo.StudyProcessVo;

/**
 * 班级Service接口
 * @author Benzolamps
 * @version 2.1.4
 * @datetime 2018-9-19 22:00:04
 */
public interface ClazzService extends BaseService<Clazz> {
    /**
     * 获取单词学习进度
     * @param clazz 班级
     * @return 学习进度
     */
    StudyProcessVo getWordStudyProcess(Clazz clazz);

    /**
     * 获取短语学习进度
     * @param clazz 班级
     * @return 学习进度
     */
    StudyProcessVo getPhraseStudyProcess(Clazz clazz);
}
