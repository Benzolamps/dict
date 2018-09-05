package com.benzolamps.dict.controller;

import com.benzolamps.dict.bean.Word;
import com.benzolamps.dict.controller.interceptor.NavigationView;
import com.benzolamps.dict.controller.interceptor.WindowView;
import com.benzolamps.dict.controller.vo.BaseVo;
import com.benzolamps.dict.controller.vo.DataVo;
import com.benzolamps.dict.controller.vo.WordVo;
import com.benzolamps.dict.dao.core.Page;
import com.benzolamps.dict.dao.core.Pageable;
import com.benzolamps.dict.service.base.WordClazzService;
import com.benzolamps.dict.service.base.WordService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Arrays;

/**
 * 单词Controller
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-9-2 23:10:10
 */
@RestController
@RequestMapping("word/")
public class WordController extends BaseController {

    @Resource
    private WordService wordService;

    @Resource
    private WordClazzService wordClazzService;

    /**
     * 列出所有单词
     * @return ModelAndView
     */
    @RequestMapping(value = "/list.html", method = {RequestMethod.GET, RequestMethod.POST})
    @NavigationView
    protected ModelAndView list(Pageable pageable) {
        ModelAndView mv = new ModelAndView("view/word/list");
        Page<Word> words = wordService.findPage(pageable);
        mv.addObject("page", words);
        return mv;
    }

    /**
     * 添加单词
     * @return ModelAndView
     */
    @RequestMapping(value = "/add.html", method = {RequestMethod.GET, RequestMethod.POST})
    @WindowView
    protected ModelAndView add() {
        ModelAndView mv = new ModelAndView("view/word/add");
        mv.addObject("wordClazzes", wordClazzService.findAll());
        return mv;
    }

    /**
     * 保存单词
     * @param wordVo 单词
     * @return 保存后的单词
     */
    @PostMapping("/save.json")
    @ResponseBody
    protected DataVo save(@RequestBody WordVo wordVo) {
        wordVo.setLibrary(1);
        Word word = WordVo.convertToWord(wordVo);
        word = wordService.persist(word);
        wordVo = WordVo.convertFromWord(word);
        return wrapperData(wordVo);
    }

    /**
     * 修改单词
     * @param id id
     * @return ModelAndView
     */
    @RequestMapping(value = "/edit.html", method = {RequestMethod.GET, RequestMethod.POST})
    @WindowView
    protected ModelAndView edit(Integer id) {
        ModelAndView mv = new ModelAndView("view/word/edit");
        mv.addObject("word", WordVo.convertFromWord(wordService.find(id)));
        mv.addObject("wordClazzes", wordClazzService.findAll());
        return mv;
    }

    /**
     * 更新单词
     * @param wordVo 单词
     * @return 更新后的单词
     */
    @PostMapping("/update.json")
    @ResponseBody
    protected DataVo update(@RequestBody WordVo wordVo) {
        wordVo.setLibrary(1);
        Word word = WordVo.convertToWord(wordVo);
        word = wordService.update(word);
        wordVo = WordVo.convertFromWord(word);
        return wrapperData(wordVo);
    }

    /**
     * 删除单词
     * @return 提示信息
     */
    @PostMapping("/delete.json")
    @ResponseBody
    protected BaseVo delete(@RequestParam("id") Integer... ids) {
        Arrays.stream(ids).forEach(wordService::remove);
        return SUCCESS_VO;
    }

    /**
     * 导入单词
     * @param file 文件
     * @return 导入成功
     */
    @PostMapping("/import.json")
    @ResponseBody
    protected BaseVo importWords(MultipartFile file) throws IOException {
        int count = wordService.imports(new InputStreamResource(file.getInputStream()));
        return wrapperData(count);
    }
}
