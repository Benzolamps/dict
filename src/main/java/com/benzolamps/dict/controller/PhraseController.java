package com.benzolamps.dict.controller;

import com.benzolamps.dict.bean.Phrase;
import com.benzolamps.dict.controller.interceptor.NavigationView;
import com.benzolamps.dict.controller.interceptor.WindowView;
import com.benzolamps.dict.controller.vo.BaseVo;
import com.benzolamps.dict.controller.vo.DataVo;
import com.benzolamps.dict.controller.vo.PhraseVo;
import com.benzolamps.dict.dao.core.Page;
import com.benzolamps.dict.dao.core.Pageable;
import com.benzolamps.dict.service.base.PhraseService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * 短语Controller
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-9-2 23:10:10
 */
@RestController
@RequestMapping("phrase/")
public class PhraseController extends BaseController {

    @Resource
    private PhraseService phraseService;

    /**
     * 列出所有短语
     * @return ModelAndView
     */
    @RequestMapping(value = "/list.html", method = {RequestMethod.GET, RequestMethod.POST})
    @NavigationView
    protected ModelAndView list(@RequestBody(required = false) Pageable pageable) {
        ModelAndView mv = new ModelAndView("view/phrase/list");
        Page<Phrase> phrases = phraseService.findPage(pageable);
        mv.addObject("page", phrases);
        return mv;
    }

    /**
     * 添加短语
     * @return ModelAndView
     */
    @RequestMapping(value = "/add.html", method = {RequestMethod.GET, RequestMethod.POST})
    @WindowView
    protected ModelAndView add() {
        return new ModelAndView("view/phrase/add");
    }

    /**
     * 保存短语
     * @param phraseVo 短语
     * @return 保存后的短语
     */
    @PostMapping("/save.json")
    @ResponseBody
    protected DataVo save(@RequestBody PhraseVo phraseVo) {
        phraseVo.setLibrary(1);
        Phrase phrase = PhraseVo.convertToPhrase(phraseVo);
        phrase = phraseService.persist(phrase);
        phraseVo = PhraseVo.convertFromPhrase(phrase);
        return wrapperData(phraseVo);
    }

    /**
     * 修改短语
     * @param id id
     * @return ModelAndView
     */
    @RequestMapping(value = "/edit.html", method = {RequestMethod.GET, RequestMethod.POST})
    @WindowView
    protected ModelAndView edit(Integer id) {
        ModelAndView mv = new ModelAndView("view/phrase/edit");
        mv.addObject("phrase", PhraseVo.convertFromPhrase(phraseService.find(id)));
        return mv;
    }

    /**
     * 更新短语
     * @param phraseVo 短语
     * @return 更新后的短语
     */
    @PostMapping("/update.json")
    @ResponseBody
    protected DataVo update(@RequestBody PhraseVo phraseVo) {
        phraseVo.setLibrary(1);
        Phrase phrase = PhraseVo.convertToPhrase(phraseVo);
        phrase = phraseService.update(phrase);
        phraseVo = PhraseVo.convertFromPhrase(phrase);
        return wrapperData(phraseVo);
    }

    /**
     * 删除短语
     * @return 提示信息
     */
    @PostMapping("/delete.json")
    @ResponseBody
    protected BaseVo delete(@RequestParam("id") Integer... ids) {
        phraseService.remove(ids);
        return SUCCESS_VO;
    }

    /**
     * 导入短语
     * @param file 文件
     * @return 导入成功
     */
    @PostMapping("/import.json")
    @ResponseBody
    protected BaseVo importPhrases(MultipartFile file) throws IOException {
        int count = phraseService.imports(new InputStreamResource(file.getInputStream()));
        return wrapperData(count);
    }


    /**
     * 检测短语是否已存在
     * @param prototype 短语原形
     * @return 检测结果
     */
    @RequestMapping(value = "/prototype_not_exists.json", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    protected boolean prototypeExists(String prototype) {
        return !phraseService.prototypeExists(prototype);
    }
}
