package com.benzolamps.dict.service.base;

import com.benzolamps.dict.component.IShuffleStrategySetup;
import com.benzolamps.dict.bean.ShuffleSolution;
import com.benzolamps.dict.controller.vo.DictPropertyInfoVo;
import com.benzolamps.dict.dao.core.Page;

import java.util.Collection;
import java.util.Set;

/**
 * 乱序策略Service接口
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-19 2018-7-20 19:33:54
 */
public interface ShuffleSolutionService {

    /** @return 获取可用的乱序策略名 */
    Set<String> getAvailableCopyStrategyNames();

    /** @return 获取默认的乱序策略实例 */
    IShuffleStrategySetup getDefaultSolutionInstance();

    /** @return 根据id获取一个乱序策略实例 */
    IShuffleStrategySetup getSolutionInstanceAt(Long id);

    /** @return 获取全部乱序方案 */
    Page<ShuffleSolution> findAll();

    /**
     * 根据id寻找乱序方案
     * @param id id
     * @return 乱序方案
     */
    ShuffleSolution find(Long id);

    /**
     * 保存一个乱序方案
     * @param shuffleSolution 乱序方案
     * @return 乱序方案
     */
    ShuffleSolution persist(ShuffleSolution shuffleSolution);

    /**
     * 修改一个乱序方案
     * @param shuffleSolution 乱序方案
     * @return 乱序方案
     */
    ShuffleSolution update(ShuffleSolution shuffleSolution) throws ClassNotFoundException;

    /**
     * 删除一个乱序方案
     * @param shuffleSolutionId 乱序方案id
     */
    void remove(Long shuffleSolutionId);

    /**
     * 根据乱序策略生成器类名获取属性
     * @param className 类名
     * @return DictPropertyInfoVo
     */
    Collection<DictPropertyInfoVo> getShuffleSolutionPropertyInfo(String className);
}
