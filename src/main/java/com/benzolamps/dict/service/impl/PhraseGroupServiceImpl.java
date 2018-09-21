package com.benzolamps.dict.service.impl;

import com.benzolamps.dict.bean.Group;
import com.benzolamps.dict.service.base.PhraseGroupService;
import org.springframework.stereotype.Service;

/**
 * 短语分组Service接口实现类
 * @author Benzolamps
 * @version 2.1.4
 * @datetime 2018-9-21 22:40:44
 */
@Service("phraseGroupService")
public class PhraseGroupServiceImpl extends GroupServiceImpl implements PhraseGroupService {

    protected PhraseGroupServiceImpl() {
        super(Group.Type.PHRASE);
    }
}
