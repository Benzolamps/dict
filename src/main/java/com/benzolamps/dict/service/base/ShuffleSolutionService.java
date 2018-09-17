package com.benzolamps.dict.service.base;

import com.benzolamps.dict.bean.ShuffleSolution;
import com.benzolamps.dict.component.IShuffleStrategySetup;
import com.benzolamps.dict.controller.vo.DictPropertyInfoVo;
import com.benzolamps.dict.dao.core.Page;
import com.benzolamps.dict.dao.core.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 乱序方案Service接口
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-19 2018-7-20 19:33:54
 */
public interface ShuffleSolutionService {

    /** @return 获取可用的乱序策略名 */
    Set<String> getAvailableStrategyNames();

    /**
     * @param id 乱序方案id
     * @return 根据id获取一个乱序策略实例
     */
    IShuffleStrategySetup getSolutionInstanceAt(Integer id);

    /** @return 获取全部乱序方案 */
    List<ShuffleSolution> findAll();

    /** @return 获取全部乱序方案 */
    Page<ShuffleSolution> findPage(Pageable pageable);

    /**
     * 根据id寻找乱序方案
     * @param id id
     * @return 乱序方案
     */
    ShuffleSolution find(Integer id);

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
    void remove(Integer shuffleSolutionId);

    /**
     * 根据乱序策略生成器类名获取属性
     * @param className 类名
     * @return DictPropertyInfoVo
     */
    Collection<DictPropertyInfoVo> getShuffleSolutionPropertyInfo(String className);

    /**
     * 获取是否还可以添加乱序方案
     * @return 结果
     */
    boolean isSpare();

    /**
     * 使用
     */
    void use(Integer id);
}
