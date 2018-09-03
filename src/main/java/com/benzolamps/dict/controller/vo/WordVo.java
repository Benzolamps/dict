package com.benzolamps.dict.controller.vo;

import com.benzolamps.dict.bean.Word;
import com.benzolamps.dict.bean.WordClazz;
import com.benzolamps.dict.component.DictPropertyInfo;
import com.benzolamps.dict.component.ExcelHeader;
import com.benzolamps.dict.service.base.LibraryService;
import com.benzolamps.dict.service.base.WordClazzService;
import com.benzolamps.dict.util.Constant;
import com.benzolamps.dict.util.DictSpring;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.util.Assert;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * 单词Vo
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-9-2 23:09:08
 */
@Data
public class WordVo implements Serializable {

    private static final long serialVersionUID = -5091224507491296666L;

    /** id */
    @Id
    private Integer id;

    /** 单词原形 */
    @ExcelHeader(value = 1, notEmpty = true)
    @NotEmpty
    @Length(max = 255)
    @DictPropertyInfo(display = "单词原形")
    private String prototype;

    /** 美式发音 */
    @ExcelHeader(value = 3, notEmpty = true)
    @NotEmpty
    @Length(max = 255)
    @DictPropertyInfo(display = "美式发音")
    private String americanPronunciation;

    /** 英式发音 */
    @ExcelHeader(value = 2, notEmpty = true)
    @NotEmpty
    @Length(max = 255)
    @DictPropertyInfo(display = "英式发音")
    private String britishPronunciation;

    /** 词性 */
    @NotNull
    @DictPropertyInfo(display = "词性")
    private Object clazzes;

    /** 词义 */
    @ExcelHeader(value = 5, notEmpty = true)
    @NotEmpty
    @Length(max = 255)
    @DictPropertyInfo(display = "词义")
    private String definition;

    /**
     * 词库
     */
    private transient Integer library;

    /** 索引 */
    @ExcelHeader(value = 0, range = @ExcelHeader.Range(min = 1))
    @NotNull
    @DictPropertyInfo(display = "索引")
    private Integer index;

    /**
     * 将WordVo转换为Word
     * @param wordVo wordVo
     * @return word
     */
    public static Word convertToWord(WordVo wordVo) {
        Assert.notNull(wordVo, "word vo不能为null");
        WordClazzService wordClazzService = DictSpring.getBean(WordClazzService.class);
        LibraryService libraryService = DictSpring.getBean(LibraryService.class);
        Word word = new Word();
        word.setId(wordVo.getId());
        word.setIndex(wordVo.getIndex());
        word.setPrototype(wordVo.getPrototype());
        word.setBritishPronunciation(wordVo.getBritishPronunciation());
        word.setAmericanPronunciation(wordVo.getAmericanPronunciation());
        word.setDefinition(wordVo.getDefinition());
        word.setLibrary(libraryService.find(wordVo.getLibrary()));
        String[] clazzArray;
        if (wordVo.getClazzes() instanceof CharSequence) {
            clazzArray = new String[] {wordVo.getClazzes().toString()};
        } else if (wordVo.getClazzes() instanceof String[]) {
            clazzArray = (String[]) wordVo.getClazzes();
        } else {
            clazzArray = Constant.EMPTY_STRING_ARRAY;
        }
        word.setClazzes(Arrays.stream(clazzArray).map(clazz -> {
            WordClazz wordClazz = wordClazzService.findByIdOrName(clazz);
            if (wordClazz == null) {
                wordClazz = new WordClazz();
                wordClazz.setName(clazz);
            }
            return wordClazz;
        }).collect(Collectors.toSet()));
        return word;
    }

    /**
     * 将Word转换为WordVo
     * @param word word
     * @return wordVo
     */
    public static WordVo convertFromWord(Word word) {
        Assert.notNull(word, "word不能为null");
        WordVo wordVo = new WordVo();
        wordVo.setId(word.getId());
        wordVo.setIndex(word.getIndex());
        wordVo.setDefinition(word.getDefinition());
        wordVo.setBritishPronunciation(word.getBritishPronunciation());
        wordVo.setAmericanPronunciation(word.getAmericanPronunciation());
        wordVo.setPrototype(word.getPrototype());
        wordVo.setClazzes(word.getClazzes().stream().map(WordClazz::getName).toArray(String[]::new));
        wordVo.setLibrary(word.getLibrary().getId());
        return wordVo;
    }
}
