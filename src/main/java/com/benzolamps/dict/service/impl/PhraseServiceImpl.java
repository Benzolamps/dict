package com.benzolamps.dict.service.impl;

import com.benzolamps.dict.bean.Group;
import com.benzolamps.dict.bean.Phrase;
import com.benzolamps.dict.controller.vo.PhraseExcelVo;
import com.benzolamps.dict.dao.base.GroupDao;
import com.benzolamps.dict.dao.core.Filter;
import com.benzolamps.dict.service.base.PhraseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collection;

/**
 * 短语Service接口实现类
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-1 22:28:07
 */
@Service("phraseService")
@Transactional
public class PhraseServiceImpl extends BaseElementServiceImpl<Phrase, PhraseExcelVo> implements PhraseService {

    @Resource
    private GroupDao groupDao;

    @Override
    public void remove(Collection<Phrase> phrases) {
        /* 删除短语前删除与各个分组的关联 */
        Collection<Group> groups = groupDao.findList(new Filter());
        for (Group group : groups) {
            group.getPhrases().removeAll(phrases);
        }
        super.remove(phrases);
    }
}
