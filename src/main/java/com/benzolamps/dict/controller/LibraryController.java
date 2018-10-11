package com.benzolamps.dict.controller;

import com.benzolamps.dict.bean.Library;
import com.benzolamps.dict.controller.interceptor.NavigationView;
import com.benzolamps.dict.controller.interceptor.WindowView;
import com.benzolamps.dict.controller.vo.BaseVo;
import com.benzolamps.dict.controller.vo.DataVo;
import com.benzolamps.dict.dao.core.Page;
import com.benzolamps.dict.dao.core.Pageable;
import com.benzolamps.dict.service.base.LibraryService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * 词库Controller
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-9-9 21:43:48
 */
@RestController
@RequestMapping("library")
public class LibraryController extends BaseController {

    @Resource
    private LibraryService libraryService;

    /**
     * 列出所有词库
     * @return ModelAndView
     */
    @RequestMapping(value = "list.html", method = {GET, POST})
    @NavigationView
    protected ModelAndView list(@RequestBody(required = false) Pageable pageable) {
        ModelAndView mv = new ModelAndView("view/library/list");
        Page<Library> libraries = libraryService.findPage(pageable);
        mv.addObject("page", libraries);
        return mv;
    }

    /**
     * 添加词库
     * @return ModelAndView
     */
    @RequestMapping(value = "add.html", method = {GET, POST})
    @WindowView
    protected ModelAndView add() {
        return new ModelAndView("view/library/add");
    }

    /**
     * 保存短语
     * @param library 词库
     * @return 保存后的词库
     */
    @PostMapping("save.json")
    @ResponseBody
    protected DataVo save(@RequestBody Library library) {
        library = libraryService.persist(library);
        return wrapperData(library);
    }

    /**
     * 修改词库
     * @param id id
     * @return ModelAndView
     */
    @RequestMapping(value = "edit.html", method = {GET, POST})
    @WindowView
    protected ModelAndView edit(Integer id) {
        ModelAndView mv = new ModelAndView("view/library/edit");
        mv.addObject("library", libraryService.find(id));
        return mv;
    }

    /**
     * 更新词库
     * @param library 词库
     * @return 更新后的词库
     */
    @PostMapping("update.json")
    @ResponseBody
    protected DataVo update(@RequestBody Library library) {
        library = libraryService.update(library);
        return wrapperData(library);
    }

    /**
     * 删除词库
     * @return 提示信息
     */
    @PostMapping("delete.json")
    @ResponseBody
    protected BaseVo delete(@RequestParam("id") Integer... ids) {
        libraryService.remove(ids);
        return SUCCESS_VO;
    }

    /**
     * 检测词库是否已存在
     * @param name 名称
     * @return 检测结果
     */
    @RequestMapping(value = "name_not_exists.json", method = {GET, POST})
    @ResponseBody
    protected boolean nameExists(String name) {
        return !libraryService.nameExists(name);
    }

    /**
     * 切换当前词库
     * @param id id
     * @return 检测结果
     */
    @RequestMapping(value = "change_current.json", method = {GET, POST})
    @ResponseBody
    protected BaseVo changeCurrent(Integer id) {
        Library library = libraryService.find(id);
        libraryService.setCurrent(library);
        return SUCCESS_VO;
    }
}
