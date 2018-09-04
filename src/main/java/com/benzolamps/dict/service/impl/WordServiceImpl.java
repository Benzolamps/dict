package com.benzolamps.dict.service.impl;

import com.benzolamps.dict.bean.Word;
import com.benzolamps.dict.controller.vo.WordExcelVo;
import com.benzolamps.dict.dao.core.Order;
import com.benzolamps.dict.dao.core.Page;
import com.benzolamps.dict.dao.core.Pageable;
import com.benzolamps.dict.service.base.LibraryService;
import com.benzolamps.dict.service.base.WordService;
import com.benzolamps.dict.util.DictExcel;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import static com.benzolamps.dict.util.DictLambda.tryFunc;

/**
 * 单词Service接口实现类
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-1 22:22:32
 */
@Service
@Transactional
public class WordServiceImpl extends BaseServiceImpl<Word> implements WordService {

    @javax.annotation.Resource
    private LibraryService libraryService;

    @Override
    @Transactional(readOnly = true)
    public Page<Word> findPage(Pageable pageable) {
        pageable.getOrders().add(Order.asc("index"));
        return super.findPage(pageable);
    }

    @Override
    @Transactional
    public void importWords(Resource resource) {
        InputStream stream = tryFunc(resource::getInputStream);
        Workbook workbook = DictExcel.inputStreamToWorkbook(stream);
        Sheet sheet = workbook.getSheetAt(0);
        List<Word> words = new LinkedList<>();
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Word word = WordExcelVo.convertToWord(DictExcel.excelRowToObject(sheet.getRow(i), WordExcelVo.class));
            word.setLibrary(libraryService.find(1));
            words.add(word);
        }
        words.forEach(this::persist);
    }
}
