package com.benzolamps.dict.dao.impl;

import com.benzolamps.dict.bean.Library;
import com.benzolamps.dict.dao.base.LibraryDao;
import com.benzolamps.dict.dao.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

/**
 * 词库Dao接口实现类
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-1 22:16:40
 */
@Repository("libraryDao")
public class LibraryDaoImpl extends BaseDaoImpl<Library> implements LibraryDao {

    // language=SpEL
    @Value(
        "#{" +
        "T(org.springframework.util.StreamUtils)" +
        ".copyToString(new org.springframework.core.io.ClassPathResource('sql/normalize_index.sql').inputStream, 'UTF-8').split('[ \\s\\u00a0]*;[ \\s\\u00a0]*')" +
        "}"
    )
    private String[] normalizeIndexSql;

    @Override
    public Page<Library> findPage(Pageable pageable) {
        DictQuery<Library> dictQuery = new GeneratedDictQuery<Library>() {
            @Override
            public void applyOrder(Order order) {
                super.applyOrder(order.convertIf(Order.SizeOrder.class, "words", "phrases"));
            }
        };
        return super.findPage(dictQuery, pageable);
    }

    @Override
    public void normalizeIndex() {
        List<Library> libraries = findList((Filter) null);
        for (Library library : libraries) {
            Stream.of(normalizeIndexSql).filter(StringUtils::hasText).forEach(sql -> executeNative(sql, Collections.singletonMap("library_id", library.getId())));
        }
    }
}
