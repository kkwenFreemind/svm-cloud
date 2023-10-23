package my.cloud.notify.modules.nms.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import my.cloud.notify.common.api.CommonPage;
import my.cloud.notify.common.api.CommonResult;
import my.cloud.notify.modules.nms.model.NmsNotificationCategory;
import my.cloud.notify.modules.nms.service.NmsNotificationLogCategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author : kevin Chang
 * @since : 2022/3/2
 *
 * 發送方式管理
 */
@Controller
@Slf4j
@Api(tags = "NmsNotificationCategoryController", description = "發送方式管理")
@RequestMapping("/snsType")
public class NmsNotificationCategoryController {

    @Autowired
    NmsNotificationLogCategoryService nmsNotificationLogCategoryService;

    @ApiOperation("查詢所有發送方式")
    @RequestMapping(value = "/listAll", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<NmsNotificationCategory>> listAll() {
        List<NmsNotificationCategory> notificationCategoryList = nmsNotificationLogCategoryService.listAll();
        return CommonResult.success(notificationCategoryList);
    }

    @ApiOperation("獲取發送分類之分頁列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CommonPage<NmsNotificationCategory>> listOrganizationPage(
            @RequestParam(required = false) String keyword, //發送分類
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {

        Page<NmsNotificationCategory> snsCateList = nmsNotificationLogCategoryService.getCateList( keyword, pageSize, pageNum);

        log.info("listSNSCatePage count: {}", CommonPage.restPage(snsCateList).getTotal());
        return CommonResult.success(CommonPage.restPage(snsCateList));
    }

    @ApiOperation("修改發送功能分類之狀態")
    @RequestMapping(value = "/updateStatus/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateStatus(@PathVariable Long id, @RequestParam(value = "status") Integer status) {

        log.info("update status: {}",status);

        boolean success = nmsNotificationLogCategoryService.updateStatus(id,status);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @ApiOperation("修改指定發送分類資訊")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult update(@PathVariable Long id, @RequestBody NmsNotificationCategory snsCate) {
        boolean success = nmsNotificationLogCategoryService.update(id, snsCate);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @ApiOperation("新增發送分類資訊")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult create(@RequestBody NmsNotificationCategory snsCate) {
        boolean success = nmsNotificationLogCategoryService.create(snsCate);
        if (success) {
            return CommonResult.success(null);
        } else {
            return CommonResult.failed();
        }
    }
}
