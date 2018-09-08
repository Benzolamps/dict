package com.benzolamps.dict.controller.vo;

import com.benzolamps.dict.bean.Phrase;
import com.benzolamps.dict.component.DictPropertyInfo;
import com.benzolamps.dict.service.base.LibraryService;
import com.benzolamps.dict.util.DictSpring;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.util.Assert;

import javax.persistence.Id;
import java.io.Serializable;

/**
 * d短语Vo
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-9-2 23:09:08
 */
@Data
public class PhraseVo implements Serializable {

    private static final long serialVersionUID = -5091224507491296666L;

    /** id */
    @Id
    private Integer id;

    /** 短语原形 */
    @NotEmpty
    @Length(max = 255)
    @DictPropertyInfo(display = "短语原形")
    private String prototype;

    /** 词义 */
    @NotEmpty
    @Length(max = 255)
    @DictPropertyInfo(display = "词义")
    private String definition;

    /** 词库 */
    private transient Integer library;

    /** 索引 */
    @DictPropertyInfo(display = "索引")
    private Integer index;

    /**
     * 将PhraseVo转换为Phrase
     * @param phraseVo phraseVo
     * @return phrase
     */
    public static Phrase convertToPhrase(PhraseVo phraseVo) {
        Assert.notNull(phraseVo, "phrase vo不能为null");
        LibraryService libraryService = DictSpring.getBean(LibraryService.class);
        Phrase phrase = new Phrase();
        phrase.setId(phraseVo.getId());
        phrase.setIndex(phraseVo.getIndex());
        phrase.setPrototype(phraseVo.getPrototype());
        phrase.setDefinition(phraseVo.getDefinition());
        phrase.setLibrary(libraryService.find(phraseVo.getLibrary()));
        return phrase;
    }

    /**
     * 将Phrase转换为phraseVo
     * @param phrase phrase
     * @return phraseVo
     */
    public static PhraseVo convertFromPhrase(Phrase phrase) {
        Assert.notNull(phrase, "phrase不能为null");
        PhraseVo phraseVo = new PhraseVo();
        phraseVo.setId(phrase.getId());
        phraseVo.setIndex(phrase.getIndex());
        phraseVo.setDefinition(phrase.getDefinition());
        phraseVo.setPrototype(phrase.getPrototype());
        phraseVo.setLibrary(phrase.getLibrary().getId());
        return phraseVo;
    }
}
