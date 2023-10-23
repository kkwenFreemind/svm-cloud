package my.cloud.backend.modules.oms.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import my.cloud.backend.common.api.CommonPage;
import my.cloud.backend.common.api.CommonResult;
import my.cloud.backend.modules.oms.model.OmsOrganization;
import my.cloud.backend.modules.oms.service.OmsOrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 單位組織架構管理
 */
@Controller
@Api(tags = "OmsOrganizationController", description = "單位組織架構管理")
@RequestMapping("/org")
@Slf4j
public class OmsOrganizationController {

    @Autowired
    private OmsOrganizationService organizationService;


    @ApiOperation("根據ID取得組織單位資訊")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<OmsOrganization> getItem(@PathVariable Long id) {
        log.info("organizationService.getById: {}",id);
        OmsOrganization organization = organizationService.getById(id);
        return CommonResult.success(organization);
    }

    @ApiOperation("獲取組織相關資訊之分頁列表")
    @RequestMapping(value = "/list/{parentId}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CommonPage<OmsOrganization>> listOrganizationPage(@PathVariable Long parentId,
                                                                          @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                                          @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {

        log.info("parentId:{}", parentId);

        Page<OmsOrganization> orgList = organizationService.list(parentId, pageSize, pageNum);
        log.info("listOrganizationPage count: {}", CommonPage.restPage(orgList).getTotal());
        return CommonResult.success(CommonPage.restPage(orgList));
    }

    @ApiOperation("添加組織")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult createOrganization(@RequestBody OmsOrganization organization) {
        organization.setCreateTime(new Date());
        boolean success = organizationService.create(organization);
        if (success) {
            log.info("createOrganization success: {}", organization);
            return CommonResult.success(null);
        }
        log.info("createOrganization failed: {}", organization);
        return CommonResult.failed();
    }

    @ApiOperation("修改組織")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateOrganization(@PathVariable Long id, @RequestBody OmsOrganization organization) {
        organization.setId(id);
        boolean success = organizationService.updateById(organization);
        if (success) {
            log.info("updateOrganization success:{}", organization);
            return CommonResult.success(null);
        }
        log.info("updateOrganization failed:{}", organization);
        return CommonResult.failed();
    }

    @ApiOperation("删除組織架構")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult deleteOrganization(@PathVariable Long id) {

        log.info("deleteOrganization: {} ", id);

        List<OmsOrganization> LowOrgList = organizationService.OrganizationLowList(id);
        log.info("LowOrgList.size(): {} ", LowOrgList.size());

        //無下層組織，可刪除
        if(LowOrgList.size() ==0) {
            boolean success = organizationService.delete(id);
            if (success) {
                log.info("deleteOrganization failed:{}", id);
                return CommonResult.success(null);
            }
        }else{
            return CommonResult.failed("存在下級單位，無法刪除！");
        }
        log.info("deleteOrganization failed:{}", id);
        return CommonResult.failed();
    }

    @ApiOperation("獲得所有組織架構")
    @RequestMapping(value = "/listAll", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<OmsOrganization>> listAllOrganization() {
        List<OmsOrganization> organizationList = organizationService.list();
        log.info("listAllOrganization count: {}", organizationList.size());
        return CommonResult.success(organizationList);
    }

    @ApiOperation("修改組織狀態")
    @RequestMapping(value = "/updateStatus/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateOrganizationStatus(@PathVariable Long id, @RequestParam(value = "status") Integer status) {
        OmsOrganization organization = new OmsOrganization();
        organization.setStatus(status);
        boolean success = organizationService.update(id, organization);
        if (success) {
            log.info("updateOrganizationStatus success: {},{}", id, organization);
            return CommonResult.success(null);
        }
        log.info("updateOrganizationStatus failed: {},{}", id, organization);
        return CommonResult.failed();
    }


    @ApiOperation("獲取組織相關資訊之分頁列表")
    @RequestMapping(value = "/listD", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CommonPage<OmsOrganization>> listOrganizationPage(
                                                                          @RequestParam(value = "pageSize", defaultValue = "100") Integer pageSize,
                                                                          @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        log.info("listOrganizationPage: {},{}",pageSize,pageNum);
        Page<OmsOrganization> orgList = organizationService.list( pageSize, pageNum);
        log.info("listOrganizationPage count: {}", CommonPage.restPage(orgList).getTotal());
        return CommonResult.success(CommonPage.restPage(orgList));
    }
}
