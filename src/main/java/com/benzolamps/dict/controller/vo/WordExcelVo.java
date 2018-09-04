package com.benzolamps.dict.controller.vo;

import com.benzolamps.dict.bean.Word;
import com.benzolamps.dict.bean.WordClazz;
import com.benzolamps.dict.component.CellFormat;
import com.benzolamps.dict.component.ExcelHeader;
import com.benzolamps.dict.service.base.WordClazzService;
import com.benzolamps.dict.util.DictSpring;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 单词Vo
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-9-2 23:09:08
 */
@Data
public class WordExcelVo implements Serializable {

    private static final long serialVersionUID = -2201903558371038672L;

    /** 索引 */
    @ExcelHeader(value = 0, cellFormat = CellFormat.INTEGER, notEmpty = true, range = @ExcelHeader.Range(min = 1))
    private Integer index;

    /** 单词原形 */
    @ExcelHeader(value = 1, notEmpty = true)
    private String prototype;

    /** 英式发音 */
    @NotEmpty
    @ExcelHeader(value = 2, notEmpty = true)
    private String britishPronunciation;

    /** 美式发音 */
    @ExcelHeader(value = 3, notEmpty = true)
    private String americanPronunciation;

    /** 词性 */
    @ExcelHeader(value = 4, notEmpty = true)
    private String clazzes;

    /** 词义 */
    @ExcelHeader(value = 5, notEmpty = true)
    private String definition;

    /**
     * 将WordExcelVo转换为word
     * @param wordExcelVo wordExcelVo
     * @return word
     */
    public static Word convertToWord(WordExcelVo wordExcelVo) {
        Word word = new Word();
        word.setPrototype(wordExcelVo.getPrototype());
        word.setAmericanPronunciation(wordExcelVo.getAmericanPronunciation());
        word.setBritishPronunciation(wordExcelVo.getBritishPronunciation());
        word.setDefinition(wordExcelVo.getDefinition());
        word.setIndex(wordExcelVo.getIndex());
        WordClazzService wordClazzService = DictSpring.getBean(WordClazzService.class);
        String[] clazzes = wordExcelVo.getClazzes().split("[ \\s\\u00a0]*[,，;；][ \\s\\u00a0]*");
        Set<WordClazz> wordClazzes = new LinkedHashSet<>();
        for (String clazz : clazzes) {
            WordClazz wordClazz = wordClazzService.findByIdOrName(clazz);
            if (wordClazz == null) {
                wordClazz = new WordClazz();
                wordClazz.setName(clazz);
                wordClazz = wordClazzService.persist(wordClazz);
            }
            wordClazzes.add(wordClazz);
        }
        word.setClazzes(wordClazzes);
        return word;
    }
}
