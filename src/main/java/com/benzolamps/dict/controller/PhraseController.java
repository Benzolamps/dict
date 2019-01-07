package com.benzolamps.dict.controller;

import com.benzolamps.dict.bean.Group;
import com.benzolamps.dict.bean.Phrase;
import com.benzolamps.dict.controller.interceptor.NavigationView;
import com.benzolamps.dict.controller.interceptor.WindowView;
import com.benzolamps.dict.controller.vo.*;
import com.benzolamps.dict.dao.core.Filter;
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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.benzolamps.dict.bean.Group.Status.NORMAL;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * 短语Controller
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-9-2 23:10:10
 */
@RestController
@RequestMapping("phrase")
public class PhraseController extends BaseController {

    @Resource
    private PhraseService phraseService;

    @Resource
    private LibraryService libraryService;

    @Resource
    private ShuffleSolutionService shuffleSolutionService;

    @Resource
    private DocSolutionService docSolutionService;

    @Resource
    private PhraseGroupService phraseGroupService;

    /**
     * 列出所有短语
     * @return ModelAndView
     */
    @RequestMapping(value = "list.html", method = {GET, POST})
    @NavigationView
    protected ModelAndView list(@RequestBody(required = false) Pageable pageable) {
        ModelAndView mv = new ModelAndView();
        if (libraryService.count() > 0) {
            mv.setViewName("view/phrase/list");
            Page<Phrase> phrases = phraseService.findPage(pageable);
            Page<PhraseVo> phraseVos = phrases.convertPage(PhraseVo::convertFromPhrase);
            mv.addObject("page", phraseVos);
            mv.addObject("maxInfo", phraseService.findMaxInfo());
        } else {
            mv.setViewName("view/library/lack");
        }
        return mv;
    }

    /**
     * 导出短语页面
     * @return ModelAndView
     */
    @RequestMapping(value = "export.html", method = {GET, POST})
    @WindowView
    protected ModelAndView export() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("view/phrase/export");
        mv.addObject("shuffleSolutions", shuffleSolutionService.findAll());
        mv.addObject("docSolutions", docSolutionService.findAll());
        return mv;
    }

    /**
     * 导出短语
     * @return ModelAndView
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @PostMapping("export_save.json")
    protected ModelAndView exportSave(@RequestBody DocExportVo docExportVo, RedirectAttributes redirectAttributes) {
        ModelAndView mv = new ModelAndView();
        Pageable pageable = docExportVo.getPageable();
        if (pageable == null) {
            Assert.notEmpty(docExportVo.getGroupIds(), "group ids不能为null或空");
            List<Group> groups = docExportVo.getGroupIds().stream().map(phraseGroupService::find)
                .peek(group -> group.getStudentsOriented().toString())
                .peek(group -> group.getPhrases().toString())
                .collect(Collectors.toList());
            docExportVo.setGroups(groups);
            mv.setViewName("redirect:/doc/export_personal.json");
        } else {
            List<Phrase> phrases = phraseService.findPage(pageable).getContent();
            mv.setViewName("redirect:/doc/export_default.json");
            if (docExportVo.getCompareStrategy() != null) {
                phrases.sort(new BaseElementComparator<>(docExportVo.getCompareStrategy()));
            }
            docExportVo.setContent(phrases);
        }
        redirectAttributes.addFlashAttribute("docExportVo", docExportVo);
        return mv;
    }

    /**
     * 添加短语
     * @return ModelAndView
     */
    @RequestMapping(value = "add.html", method = {GET, POST})
    @WindowView
    protected ModelAndView add() {
        Assert.isTrue(libraryService.count() > 0, "未选择词库");
        return new ModelAndView("view/phrase/add");
    }

    /**
     * 保存短语
     * @param phraseVo 短语
     * @return 保存后的短语
     */
    @PostMapping("save.json")
    @ResponseBody
    protected DataVo save(@RequestBody PhraseVo phraseVo) {
        Assert.isTrue(libraryService.count() > 0, "未选择词库");
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
    @RequestMapping(value = "edit.html", method = {GET, POST})
    @WindowView
    protected ModelAndView edit(Integer id) {
        Assert.isTrue(libraryService.count() > 0, "未选择词库");
        ModelAndView mv = new ModelAndView("view/phrase/edit");
        mv.addObject("phrase", PhraseVo.convertFromPhrase(phraseService.find(id)));
        return mv;
    }

    /**
     * 更新短语
     * @param phraseVo 短语
     * @return 更新后的短语
     */
    @PostMapping("update.json")
    @ResponseBody
    protected DataVo update(@RequestBody PhraseVo phraseVo) {
        Assert.isTrue(libraryService.count() > 0, "未选择词库");
        Phrase phrase = PhraseVo.convertToPhrase(phraseVo);
        phrase = phraseService.update(phrase);
        phraseVo = PhraseVo.convertFromPhrase(phrase);
        return wrapperData(phraseVo);
    }

    /**
     * 删除短语
     * @return 提示信息
     */
    @PostMapping("delete.json")
    @ResponseBody
    protected BaseVo delete(@RequestParam("id") Integer... ids) {
        Assert.isTrue(libraryService.count() > 0, "未选择词库");
        phraseService.remove(ids);
        return SUCCESS_VO;
    }

    /**
     * 导入短语
     * @param file 文件
     * @return 导入成功
     */
    @PostMapping("import.json")
    @ResponseBody
    protected BaseVo importPhrases(MultipartFile file) throws IOException {
        Assert.isTrue(libraryService.count() > 0, "未选择词库");
        int count = phraseService.imports(new InputStreamResource(file.getInputStream()));
        return wrapperData(count);
    }

    /**
     * 检测短语是否已存在
     * @param prototype 短语原形
     * @return 检测结果
     */
    @RequestMapping(value = "prototype_not_exists.json", method = {GET, POST})
    @ResponseBody
    protected boolean prototypeExists(String prototype) {
        Assert.isTrue(libraryService.count() > 0, "未选择词库");
        return !phraseService.prototypeExists(prototype);
    }

    /**
     * 添加到分组
     * @param phraseIds 短语id
     * @return ModelAndView
     */
    @WindowView
    @GetMapping("add_to.html")
    protected ModelAndView addTo(@RequestParam("phraseId") Integer... phraseIds) {
        ModelAndView mv = new ModelAndView("view/phrase/add_to");
        mv.addObject("groups", phraseGroupService.findList(Filter.eq("status", NORMAL)));
        mv.addObject("phrases", phraseService.findList(Filter.in("id", Arrays.asList(phraseIds))));
        return mv;
    }
}
