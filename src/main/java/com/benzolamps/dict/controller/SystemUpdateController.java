package com.benzolamps.dict.controller;

import com.benzolamps.dict.controller.vo.BaseVo;
import com.benzolamps.dict.service.base.VersionService;
import com.benzolamps.dict.util.Constant;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.singletonMap;

/**
 * 系统更新控制器
 * @author Benzolamps
 * @datetime 2018-9-15 10:36:31
 */
@SuppressWarnings("ScheduledMethodInspection")
@RestController
@RequestMapping("system/")
public class SystemUpdateController extends BaseController {

    @Resource
    private VersionService versionService;

    @Resource
    private SimpMessagingTemplate messagingTemplate;

    @PostConstruct
    private void postConstruct() {
        versionService.setStatusCallback(status -> {
            Map<String, Object> result = new HashMap<>();
            switch (status) {
                case HAS_NEW:
                    result.put("newVersionName", versionService.getNewVersionName());
                    messagingTemplate.convertAndSend("/version/has_new", wrapperData(result));
                    break;
                case ALREADY_NEW:
                    messagingTemplate.convertAndSend("/version/already_new", wrapperData(result));
                    break;
                case DOWNLOADING:
                    messagingTemplate.convertAndSend("/version/downloading", wrapperData(result));
                    break;
                case DOWNLOADED:
                    result.put("total", versionService.getTotal());
                    result.put("totalSize", versionService.getTotalSize());
                    result.put("deltaTime", versionService.getDeltaTime());
                    messagingTemplate.convertAndSend("/version/downloaded", wrapperData(result));
                    break;
                case INSTALLED:
                    result.put("total", versionService.getTotal());
                    result.put("totalSize", versionService.getTotalSize());
                    result.put("deltaTime", versionService.getDeltaTime());
                    messagingTemplate.convertAndSend("/version/installed", wrapperData(result));
                    break;
                case FAILED:
                    messagingTemplate.convertAndSend("/version/failed", wrapperData(result));
                    break;
            }
        });
    }

    /**
     * 检查新版本
     * @return 是否有新版本
     */
    @Scheduled(fixedRate = 1000 * 60 * 15)
    @RequestMapping(value = "/check_new_version.json", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    protected BaseVo checkNewVersion() {
        boolean hasNew = !versionService.isDead() && versionService.getStatus() == VersionService.Status.HAS_NEW;
        if (hasNew) {
            Map<String, Object> result = singletonMap("newVersionName", versionService.getNewVersionName());
            messagingTemplate.convertAndSend("/version/has_new", wrapperData(result));
        }
        return SUCCESS_VO;
    }

    /**
     * 检查是否下载完成
     * @return 是否下载完成
     */
    @RequestMapping(value = "/check_downloaded.json", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    protected BaseVo checkDownloaded() {
        boolean downloaded = !versionService.isDead() && versionService.getStatus() == VersionService.Status.DOWNLOADED;
        if (downloaded) {
            Map<String, Object> result = new HashMap<>();
            result.put("total", versionService.getTotal());
            result.put("totalSize", versionService.getTotalSize());
            result.put("deltaTime", versionService.getDeltaTime());
            messagingTemplate.convertAndSend("/version/downloaded", wrapperData(result));
        }
        return SUCCESS_VO;
    }

    /**
     * 检查是否安装完成
     * @return 是否安装完成
     */
    @RequestMapping(value = "/check_installed.json", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    protected BaseVo checkInstalled() {
        boolean installed = !versionService.isDead() && versionService.getStatus() == VersionService.Status.INSTALLED;
        if (installed) {
            Map<String, Object> result = new HashMap<>();
            result.put("total", versionService.getTotal());
            result.put("totalSize", versionService.getTotalSize());
            result.put("deltaTime", versionService.getDeltaTime());
            messagingTemplate.convertAndSend("/version/installed", wrapperData(result));
        }
        return SUCCESS_VO;
    }

    /**
     * 检查是否更新失败
     * @return 是否更新失败
     */
    @RequestMapping(value = "/check_failed.json", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    protected BaseVo checkFailed() {
        boolean failed = !versionService.isDead() && versionService.getStatus() == VersionService.Status.FAILED;
        if (failed) {
            messagingTemplate.convertAndSend("/version/failed", Constant.EMPTY_MAP);
        }
        return SUCCESS_VO;
    }

    /**
     * 禁用
     * @return 状态
     */
    @RequestMapping(value = "/die.json", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    protected BaseVo die() {
        versionService.die();
        return SUCCESS_VO;
    }

    /**
     * 系统更新界面
     * @return ModelAndView
     */
    @RequestMapping(value = "/update.html", method = {RequestMethod.GET, RequestMethod.POST})
    protected ModelAndView update() {
        ModelAndView mv = new ModelAndView("view/system/update");
        mv.addObject("status", versionService.getStatus());
        mv.addObject("newVersionName", versionService.getNewVersionName());
        mv.addObject("total", versionService.getTotal());
        mv.addObject("totalSize", versionService.getTotalSize());
        mv.addObject("deltaTime", versionService.getDeltaTime());
        return mv;
    }

    /**
     * 系统更新开始
     * @return 开始
     */
    @RequestMapping(value = "/update.json", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    protected BaseVo updateProcess() {
        versionService.update();
        return SUCCESS_VO;
    }
}
