package com.benzolamps.dict.controller;

import com.benzolamps.dict.bean.Word;
import com.benzolamps.dict.controller.interceptor.NavigationView;
import com.benzolamps.dict.controller.interceptor.WindowView;
import com.benzolamps.dict.controller.vo.BaseVo;
import com.benzolamps.dict.controller.vo.DataVo;
import com.benzolamps.dict.controller.vo.DocExportVo;
import com.benzolamps.dict.controller.vo.WordVo;
import com.benzolamps.dict.dao.core.Page;
import com.benzolamps.dict.dao.core.Pageable;
import com.benzolamps.dict.service.base.*;
import org.springframework.core.io.InputStreamResource;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

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

    @Resource
    private LibraryService libraryService;

    @Resource
    private ShuffleSolutionService shuffleSolutionService;

    @Resource
    private DocSolutionService docSolutionService;

    /**
     * 列出所有单词
     * @return ModelAndView
     */
    @RequestMapping(value = "/list.html", method = {RequestMethod.GET, RequestMethod.POST})
    @NavigationView
    protected ModelAndView list(@RequestBody(required = false) Pageable pageable) {
        ModelAndView mv = new ModelAndView();
        if (libraryService.count() > 0) {
            mv.setViewName("view/word/list");
            Page<Word> words = wordService.findPage(pageable);
            mv.addObject("wordClazzes", wordClazzService.findAll());
            mv.addObject("page", words);
        } else {
            mv.setViewName("view/library/lack");
        }
        return mv;
    }

    /**
     * 导出单词页面
     * @return ModelAndView
     */
    @RequestMapping(value = "/export.html", method = {RequestMethod.GET, RequestMethod.POST})
    @WindowView
    protected ModelAndView export() {
        ModelAndView mv = new ModelAndView();
        if (libraryService.count() > 0) {
            mv.setViewName("view/word/export");
            mv.addObject("shuffleSolutions", shuffleSolutionService.findAll());
            mv.addObject("docSolutions", docSolutionService.findAll());
        } else {
            mv.setViewName("view/library/lack");
        }
        return mv;
    }

    /**
     * 导出单词
     * @return ModelAndView
     */
    @PostMapping(value = "/export_save.json")
    protected ModelAndView exportSave(@RequestBody DocExportVo docExportVo, RedirectAttributes redirectAttributes) {
        ModelAndView mv = new ModelAndView();
        Pageable pageable = docExportVo.getPageable();
        List<Word> words;
        if (pageable == null) {
            words = wordService.findAll();
        } else {
            words = wordService.findPage(pageable).getContent();
        }
        docExportVo.setContent(words);
        redirectAttributes.addFlashAttribute("docExportVo", docExportVo);
        mv.setViewName("redirect:/doc/export.json");
        return mv;
    }

    /**
     * 添加单词
     * @return ModelAndView
     */
    @RequestMapping(value = "/add.html", method = {RequestMethod.GET, RequestMethod.POST})
    @WindowView
    protected ModelAndView add() {
        Assert.isTrue(libraryService.count() > 0, "未选择词库");
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
        Assert.isTrue(libraryService.count() > 0, "未选择词库");
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
        Assert.isTrue(libraryService.count() > 0, "未选择词库");
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
        Assert.isTrue(libraryService.count() > 0, "未选择词库");
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
        Assert.isTrue(libraryService.count() > 0, "未选择词库");
        wordService.remove(ids);
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
        Assert.isTrue(libraryService.count() > 0, "未选择词库");
        int count = wordService.imports(new InputStreamResource(file.getInputStream()));
        return wrapperData(count);
    }

    /**
     * 检测单词是否已存在
     * @param prototype 单词原形
     * @return 检测结果
     */
    @RequestMapping(value = "/prototype_not_exists.json", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    protected boolean prototypeNotExists(String prototype) {
        Assert.isTrue(libraryService.count() > 0, "未选择词库");
        return !wordService.prototypeExists(prototype);
    }
}
