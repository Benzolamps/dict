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
        DictQuery<Phrase> dictQuery = new GeneratedDictQuery<Phrase>(Phrase.class) {
            @Override
            public void applyOrder(Order order) {
                if ("prototype".equals(order.getField())) {
                    super.applyOrder(new Order.IgnoreCaseOrder(order.getField(), order.getDirection()));
                } else {
                    super.applyOrder(order);
                }
            }
        };
        return super.findPage(dictQuery, pageable);
    }
}
