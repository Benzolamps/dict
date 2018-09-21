package com.benzolamps.dict.service.impl;

import com.benzolamps.dict.bean.Group;
import com.benzolamps.dict.service.base.WordGroupService;
import org.springframework.stereotype.Service;

/**
 * 单词分组Service接口实现类
 * @author Benzolamps
 * @version 2.1.4
 * @datetime 2018-9-21 22:40:39
 */
@Service("wordGroupService")
public class WordGroupServiceImpl extends GroupServiceImpl implements WordGroupService {

    protected WordGroupServiceImpl() {
        super(Group.Type.WORD);
    }
}
