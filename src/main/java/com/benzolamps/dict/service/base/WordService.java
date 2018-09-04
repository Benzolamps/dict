package com.benzolamps.dict.service.base;

import com.benzolamps.dict.bean.Word;
import org.springframework.core.io.Resource;

/**
 * 单词Service接口
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-1 22:21:02
 */
public interface WordService extends BaseService<Word> {

    /**
     * 导入单词
     * @param resource 资源
     */
    void importWords(Resource resource);
}
