package com.benzolamps.dict.controller;

import com.benzolamps.dict.controller.vo.BaseVo;
import com.benzolamps.dict.service.base.VersionService;
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
                    result.put("hasNew", true);
                    result.put("versionName", versionService.getNewVersionName());
                    messagingTemplate.convertAndSend("/version/has_new", wrapperData(result));
                    break;
                case ALREADY_NEW:
                    result.put("alreadyNew", true);
                    messagingTemplate.convertAndSend("/version/already_new", wrapperData(result));
                    break;
                case DOWNLOADING:
                    result.put("downloading", true);
                    messagingTemplate.convertAndSend("/version/downloading", wrapperData(result));
                    break;
                case COPYING:
                    result.put("copying", true);
                    messagingTemplate.convertAndSend("/version/copying", wrapperData(result));
                    break;
                case DELETING:
                    result.put("deleting", true);
                    messagingTemplate.convertAndSend("/version/deleting", wrapperData(result));
                    break;
                case FINISHED:
                    result.put("finished", true);
                    result.put("total", versionService.getTotal());
                    result.put("totalSize", versionService.getTotalSize());
                    result.put("deltaTime", versionService.getDeltaTime());
                    messagingTemplate.convertAndSend("/version/finished", wrapperData(result));
                    break;
                case FAILED:
                    result.put("failed", true);
                    messagingTemplate.convertAndSend("/version/failed", wrapperData(result));
                    break;
            }
        });
    }

    /**
     * 检查新版本
     * @return 是否有新版本
     */
    @Scheduled(fixedRate = 1000 * 60 * 10)
    @RequestMapping(value = "/check_new_version.json", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    protected BaseVo checkNewVersion() {
        boolean hasNew = versionService.getStatus() == VersionService.Status.HAS_NEW;
        if (hasNew) {
            Map<String, Object> result = new HashMap<>();
            result.put("hasNew", hasNew);
            result.put("versionName", versionService.getNewVersionName());
            messagingTemplate.convertAndSend("/version/has_new", wrapperData(result));
        }
        return SUCCESS_VO;
    }

    /**
     * 检查是否更新完成
     * @return 是否更新完成
     */
    @RequestMapping(value = "/check_update_finished.json", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    protected BaseVo checkUpdateFinished() {
        boolean finished = versionService.getStatus() == VersionService.Status.FINISHED;
        if (finished) {
            Map<String, Object> result = new HashMap<>();
            result.put("finished", finished);
            result.put("total", versionService.getTotal());
            result.put("totalSize", versionService.getTotalSize());
            result.put("deltaTime", versionService.getDeltaTime());
            messagingTemplate.convertAndSend("/version/finished", wrapperData(result));
        }
        return SUCCESS_VO;
    }

    /**
     * 检查是否更新失败
     * @return 是否更新失败
     */
    @RequestMapping(value = "/check_update_failed.json", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    protected BaseVo checkUpdateFailed() {
        boolean finished = versionService.getStatus() == VersionService.Status.FAILED;
        if (finished) {
            Map<String, Object> result = new HashMap<>();
            result.put("failed", finished);
            messagingTemplate.convertAndSend("/version/failed", wrapperData(result));
        }
        return SUCCESS_VO;
    }

    /**
     * 重置更新状态
     * @return 状态
     */
    @RequestMapping(value = "/reset_status.json", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    protected BaseVo resetStatus() {
        versionService.resetStatus();
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
