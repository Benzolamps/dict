package com.benzolamps.dict.service.base;

import com.benzolamps.dict.bean.BaseElement;
import org.springframework.core.io.Resource;

import java.util.Collection;

/**
 * 单词或短语类的基类Service接口
 * @param <T> 类型
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-9-5 21:05:33
 */
@SuppressWarnings("BooleanMethodIsAlwaysInverted")
public interface BaseElementService<T extends BaseElement> extends BaseService<T> {

    /**
     * 导入单词或短语
     * @param resource 资源
     * @return 导入的个数
     */
    int imports(Resource resource);

    /**
     * 单词原形或短语原形是否存在
     * @param prototype 原形
     * @return 结果
     */
    boolean prototypeExists(String prototype);

    /**
     * 通过原形查找单词或短语
     * @param prototype 原形
     * @return 单词或短语
     */
    T findByPrototype(String prototype);

    /**
     * 通过原形查找单词或短语
     * @param prototypes 原形
     * @return 单词或短语集合
     */
    Collection<T> findByPrototypes(Collection<String> prototypes);
}
