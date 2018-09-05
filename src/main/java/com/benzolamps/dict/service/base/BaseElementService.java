package com.benzolamps.dict.service.base;

import com.benzolamps.dict.bean.BaseElement;
import org.springframework.core.io.Resource;

/**
 * 单词或短语类的基类Service接口
 * @param <T> 类型
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-9-5 21:05:33
 */
public interface BaseElementService<T extends BaseElement> extends BaseService<T> {

    /**
     * 导入单词或短语
     * @param resource 资源
     * @return 导入的个数
     */
    int imports(Resource resource);

    boolean contains(String prototype);
}
