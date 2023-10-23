package my.cloud.notify.modules.nms.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import lombok.extern.slf4j.Slf4j;
import my.cloud.notify.common.api.CommonPage;
import my.cloud.notify.common.api.CommonResult;
import my.cloud.notify.modules.nms.model.NmsNotificationLog;
import my.cloud.notify.modules.nms.service.NmsNotificationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author : kevin Chang
 * @since : 2022/2/24
 * <p>
 * 發送紀錄管理
 */

@Controller
@Slf4j
@Api(tags = "NmsNotificationLogController", description = "發送紀錄管理")
@RequestMapping("/sns")
public class NmsNotificationLogController {

    @Autowired
    private NmsNotificationLogService nmsNotificationLogService;

//    @ApiOperation("獲取發送紀錄之分頁列表")
//    @RequestMapping(value = "/list", method = RequestMethod.GET)
//    @ResponseBody
//    public CommonResult<CommonPage<NmsNotificationLog>> listKK(@RequestParam(required = false) Integer status, // 發送狀態
//                                                              @RequestParam(required = false) String keyword, //發送內容
//                                                              @RequestParam(required = false) String name, //發送方式
//                                                              @RequestParam(required = false) String createTime, //發送方式
//                                                              @RequestParam(required = false) String beginTime, //起日
//                                                              @RequestParam(required = false) String endTime, //迄日
//                                                              @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
//                                                              @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
//
//        Page<NmsNotificationLog> orgList = nmsNotificationLogService.getNotificationLogAndCate(status, keyword, name, beginTime, endTime, pageSize, pageNum);
//
//        log.info("listOrganizationPage count: {}", CommonPage.restPage(orgList).getTotal());
//        return CommonResult.success(CommonPage.restPage(orgList));
////        List<NmsNotificationVO> notificationLogList = nmsNotificationLogService.getNotificationLogAndCate();
////        return notificationLogList;
//    }

    @ApiOperation("查詢所有發送紀錄")
    @RequestMapping(value = "/listAll", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<NmsNotificationLog>> listAll() {
        List<NmsNotificationLog> notificationLogList = nmsNotificationLogService.listAll();
        return CommonResult.success(notificationLogList);
    }

    @ApiOperation("新增發送紀錄")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult create(@RequestBody NmsNotificationLog nmsNotificationLog) {
        boolean success = nmsNotificationLogService.create(nmsNotificationLog);
        if (success) {
            return CommonResult.success(null);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("獲取發送紀錄之分頁列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CommonPage<NmsNotificationLog>> listOrganizationPage(
            @RequestParam(required = false) Integer status, // 發送狀態
            @RequestParam(required = false) String keyword, //發送內容
            @RequestParam(required = false) String name, //發送方式
            @RequestParam(required = false) String createTime, //發送方式
            @RequestParam(required = false) String beginTime, //起日
            @RequestParam(required = false) String endTime, //迄日
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {

        Page<NmsNotificationLog> orgList = nmsNotificationLogService.getLogList(status, keyword, name, beginTime, endTime, pageSize, pageNum);

        log.info("listOrganizationPage count: {}", CommonPage.restPage(orgList).getTotal());
        return CommonResult.success(CommonPage.restPage(orgList));
    }


}
