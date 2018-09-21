package com.benzolamps.dict.dao.impl;

import com.benzolamps.dict.bean.Phrase;
import com.benzolamps.dict.dao.base.PhraseDao;
import com.benzolamps.dict.dao.core.*;
import org.springframework.stereotype.Repository;

/**
 * 短语Dao接口实现类
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-1 23:01:56
 */
@Repository("phraseDao")
public class PhraseDaoImpl extends BaseElementDaoImpl<Phrase> implements PhraseDao {
    @Override
    public Page<Phrase> findPage(Pageable pageable) {
        DictQuery<Phrase> dictQuery = new GeneratedDictQuery<Phrase>() {
            @Override
            public void applyOrder(Order order) {
                super.applyOrder(order.convertIf(Order.IgnoreCaseOrder.class, "prototype"));
            }
        };
        return super.findPage(dictQuery, pageable);
    }
}
