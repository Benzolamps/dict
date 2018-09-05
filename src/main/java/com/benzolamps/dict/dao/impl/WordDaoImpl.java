package com.benzolamps.dict.dao.impl;

import com.benzolamps.dict.bean.Word;
import com.benzolamps.dict.bean.WordClazz;
import com.benzolamps.dict.dao.base.BaseElementDao;
import com.benzolamps.dict.dao.base.WordDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * 单词Dao接口实现类
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-1 22:13:15
 */
@Repository("wordDao")
public class WordDaoImpl extends BaseElementDaoImpl<Word> implements WordDao {

    @Override
    public List<Word> searchWords(Integer start, Integer end, String definition, WordClazz clazz) {
//        StringBuilder builder = new StringBuilder();
//        builder.append("select word from Word as word where 1 = 1 ");
//        if (definition != null) builder.append("and word.definition like :definition ");
//        if (clazz != null) builder.append("and :clazz in (word.clazzes)");
//        return findList(start, end - start + 1, builder.toString(), new HashMap<String, Object>() {{
//            put("definition", definition);
//            put("clazz", clazz);
//        }});

        return null;
    }
}
