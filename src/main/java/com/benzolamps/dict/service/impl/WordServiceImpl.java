package com.benzolamps.dict.service.impl;

import com.benzolamps.dict.bean.Word;
import com.benzolamps.dict.bean.WordClazz;
import com.benzolamps.dict.controller.vo.WordExcelVo;
import com.benzolamps.dict.service.base.WordClazzService;
import com.benzolamps.dict.service.base.WordService;
import lombok.SneakyThrows;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 单词Service接口实现类
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-1 22:22:32
 */
@Service("wordService")
@Transactional
public class WordServiceImpl extends BaseElementServiceImpl<Word, WordExcelVo> implements WordService {

    @Resource
    private WordClazzService wordClazzService;

    @Override
    public void persist(Word... words) {
        List<WordClazz> wordClazzes = wordClazzService.findAll();
        for (Word word : words) {
            if (applyWord(wordClazzes, word)) {
                wordClazzes = wordClazzService.findAll();
            }
        }
        super.persist(words);
    }

    @Override
    public void persist(Collection<Word> words) {
        List<WordClazz> wordClazzes = wordClazzService.findAll();
        for (Word word : words) {
            if (applyWord(wordClazzes, word)) {
                wordClazzes = wordClazzService.findAll();
            }
        }
        super.persist(words);
    }

    @Override
    public Word update(Word word, String... ignoreProperties) {
        List<WordClazz> wordClazzes = wordClazzService.findAll();
        applyWord(wordClazzes, word);
        return super.update(word, ignoreProperties);
    }

    @Override
    public Word persist(Word word) {
        List<WordClazz> wordClazzes = wordClazzService.findAll();
        applyWord(wordClazzes, word);
        return super.persist(word);
    }

    @Override
    public void update(Collection<Word> words, String... ignoreProperties) {
        List<WordClazz> wordClazzes = wordClazzService.findAll();
        for (Word word : words) {
            if (applyWord(wordClazzes, word)) {
                wordClazzes = wordClazzService.findAll();
            }
        }
        super.update(words, ignoreProperties);
    }

    private boolean applyWord(Collection<WordClazz> wordClazzes, Word word) {
        boolean added = false;
        Set<WordClazz> clazzes = word.getClazzes();
        Set<WordClazz> newClazzes = new LinkedHashSet<>();
        for (WordClazz clazz : clazzes) {
            WordClazz newClazz = getExist(wordClazzes, clazz.getName());
            if (newClazz == null) {
                newClazz = new WordClazz();
                newClazz.setName(clazz.getName());
                newClazz = wordClazzService.persist(newClazz);
                added = true;
            }
            newClazzes.add(newClazz);
        }
        word.setClazzes(newClazzes);
        return added;
    }

    private WordClazz getExist(Collection<WordClazz> clazzes, String name) {
        try {
            return clazzes.stream().filter(clazz -> clazz.getId().equals(Integer.valueOf(name))).findFirst().orElse(null);
        } catch (NumberFormatException e) {
            return clazzes.stream().filter(clazz -> clazz.getName().equals(name)).findFirst().orElse(null);
        }
    }

    @Override
    @SneakyThrows(IOException.class)
    public void wordsToExcel(List<Word> words, OutputStream outputStream) {
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        HSSFSheet sheet = hssfWorkbook.createSheet("Extra Words");
        HSSFRow header = sheet.createRow(0);
        header.createCell(0).setCellValue("索引");
        header.createCell(1).setCellValue("单词原形");
        header.createCell(1).setCellValue("英式发音");
        header.createCell(1).setCellValue("美式发音");
        header.createCell(1).setCellValue("词性");
        header.createCell(1).setCellValue("词义");
        for (int i = 0; i < words.size(); i++) {
            Word word = words.get(i);
            HSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(word.getIndex() == null ? i + 1 : word.getIndex());
            row.createCell(1).setCellValue(word.getPrototype());
            row.createCell(2).setCellValue(word.getBritishPronunciation());
            row.createCell(3).setCellValue(word.getAmericanPronunciation());
            row.createCell(4).setCellValue(
                CollectionUtils.isEmpty(word.getClazzes()) ?
                    null :
                    word.getClazzes().stream().map(WordClazz::getName).collect(Collectors.joining("，", "", ""))
            );
            row.createCell(5).setCellValue(word.getDefinition());
        }
        try (OutputStream ops = outputStream) {
            hssfWorkbook.write(ops);
        }
    }
}
