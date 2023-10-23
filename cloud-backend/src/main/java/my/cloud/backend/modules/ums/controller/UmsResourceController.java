package my.cloud.backend.modules.ums.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import my.cloud.backend.common.api.CommonPage;
import my.cloud.backend.common.api.CommonResult;
import my.cloud.backend.modules.ums.model.UmsResource;
import my.cloud.backend.modules.ums.service.UmsResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * 後台資源管理
 * Created by Kevin Chang on 2022/7/5.
 **/
@Controller
@Api(tags = "UmsResourceController", description = "後台資源管理")
@RequestMapping("/resource")
public class UmsResourceController {

    @Autowired
    private UmsResourceService resourceService;

    @ApiOperation("添加後台資源")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult create(@RequestBody UmsResource umsResource) {
        boolean success = resourceService.create(umsResource);
        if (success) {
            return CommonResult.success(null);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("修改後台資源")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult update(@PathVariable Long id,
                               @RequestBody UmsResource umsResource) {
        boolean success = resourceService.update(id, umsResource);
        if (success) {
            return CommonResult.success(null);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("根據ID獲得資源详情")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<UmsResource> getItem(@PathVariable Long id) {
        UmsResource umsResource = resourceService.getById(id);
        return CommonResult.success(umsResource);
    }

    @ApiOperation("根據ID删除後台資源")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult delete(@PathVariable Long id) {
        boolean success = resourceService.delete(id);
        if (success) {
            return CommonResult.success(null);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("分頁模糊查詢後台資源")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CommonPage<UmsResource>> list(@RequestParam(required = false) Long categoryId,
                                                      @RequestParam(required = false) String nameKeyword,
                                                      @RequestParam(required = false) String urlKeyword,
                                                      @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                      @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        Page<UmsResource> resourceList = resourceService.list(categoryId,nameKeyword, urlKeyword, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(resourceList));
    }

    @ApiOperation("查詢所有後台資源")
    @RequestMapping(value = "/listAll", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<UmsResource>> listAll() {
        List<UmsResource> resourceList = resourceService.list();
        return CommonResult.success(resourceList);
    }


}
