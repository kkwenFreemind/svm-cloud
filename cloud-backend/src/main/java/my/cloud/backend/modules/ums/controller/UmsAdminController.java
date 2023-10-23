package my.cloud.backend.modules.ums.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import my.cloud.backend.common.api.CommonPage;
import my.cloud.backend.common.api.CommonResult;
import my.cloud.backend.common.constant.AuthConstant;
import my.cloud.backend.common.domain.UserDto;
import my.cloud.backend.common.exception.Asserts;
import my.cloud.backend.modules.oms.model.OmsOrganization;
import my.cloud.backend.modules.oms.service.OmsOrganizationService;
import my.cloud.backend.modules.ums.dto.UmsAdminLoginParam;
import my.cloud.backend.modules.ums.dto.UmsAdminParam;
import my.cloud.backend.modules.ums.dto.UpdateAdminPasswordParam;
import my.cloud.backend.modules.ums.model.UmsAdmin;
import my.cloud.backend.modules.ums.model.UmsRole;
import my.cloud.backend.modules.ums.service.UmsAdminService;
import my.cloud.backend.modules.ums.service.UmsRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 後台帳號管理
 * Created by Kevin Chang on 2022/7/5.
 **/
@Controller
@Api(tags = "UmsAdminController", description = "後台帳號管理")
@RequestMapping("/admin")
@Slf4j
public class UmsAdminController {

    @Autowired
    private UmsAdminService adminService;
    @Autowired
    private UmsRoleService roleService;

    @Autowired
    private OmsOrganizationService omsOrganizationService;

    @ApiOperation(value = "用戶註冊")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<UmsAdmin> register(@Validated @RequestBody UmsAdminParam umsAdminParam) {

        //取得組織代碼
        log.info("umsAdminParam :{}",umsAdminParam.getOrgId());
        OmsOrganization omsOrganization = new OmsOrganization();
        omsOrganization =omsOrganizationService.getById(umsAdminParam.getOrgId());

        log.info("omsOrganization info: {},{}" , omsOrganization.getNameSn(),omsOrganization.getName());
        umsAdminParam.setOrgSn(omsOrganization.getNameSn());
        umsAdminParam.setOrgName(omsOrganization.getName());

        UmsAdmin umsAdmin = adminService.register(umsAdminParam);
        if (umsAdmin == null) {
            return CommonResult.failed();
        }

        //測試：發送訊息到message queue
//        int snsType=0;
//        String message="您的帳號："+umsAdminParam.getUsername() +"已創建，密碼為："+umsAdminParam.getPassword()+",謝謝";
//        String target="";
//
//        if(!umsAdminParam.getMobile().isEmpty()) {
//            target = umsAdminParam.getMobile();
//            snsType=2;
//            SendMessageParam sendMessageParam = new SendMessageParam(snsType, message, target);
//            rabbitTemplate.convertAndSend("tpu.queue", sendMessageParam);
//        }
//
//        if(!umsAdminParam.getEmail().isEmpty()) {
//            target = umsAdminParam.getEmail();
//            snsType=3;
//            SendMessageParam sendMessageParam = new SendMessageParam(snsType, message, target);
//            rabbitTemplate.convertAndSend("tpu.queue", sendMessageParam);
//        }


        return CommonResult.success(umsAdmin);
    }

    @ApiOperation(value = "登入以及取得Token")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult login(@Validated @RequestBody UmsAdminLoginParam umsAdminLoginParam) {
        log.info("/admin/login -->" + umsAdminLoginParam.getUsername());
        return adminService.login(umsAdminLoginParam.getUsername(), umsAdminLoginParam.getPassword());
    }

    @ApiOperation("根據用戶名稱取得用戶訊息")
    @RequestMapping(value = "/loadByUsername", method = RequestMethod.GET)
    @ResponseBody
    public UserDto loadUserByUsername(@RequestParam String username) {
        UserDto userDTO = adminService.loadUserByUsername(username);
        return userDTO;
    }

    @ApiOperation(value = "取得當前登入用戶訊息")
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult getAdminInfo(Principal principal) {
        if (principal == null) {
            return CommonResult.unauthorized(null);
        }
        String username = principal.getName();
        UmsAdmin umsAdmin = adminService.getAdminByUsername(username);
        Map<String, Object> data = new HashMap<>();
        data.put("username", umsAdmin.getUsername());
        data.put("menus", roleService.getMenuList(umsAdmin.getId()));
        data.put("icon", umsAdmin.getIcon());
        List<UmsRole> roleList = adminService.getRoleList(umsAdmin.getId());
        if (CollUtil.isNotEmpty(roleList)) {
            List<String> roles = roleList.stream().map(UmsRole::getName).collect(Collectors.toList());
            data.put("roles", roles);
        }
        return CommonResult.success(data);
    }

    @ApiOperation(value = "登出")
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult logout() {
        return CommonResult.success(null);
    }

    @ApiOperation("獲取用戶帳號或姓名相關資訊之分頁列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CommonPage<UmsAdmin>> list(@RequestParam(value = "keyword", required = false) String keyword,
                                                   @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                   @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        Page<UmsAdmin> adminList = adminService.list(keyword, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(adminList));
    }


    @ApiOperation("取得指定用戶帳號資訊")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<UmsAdmin> getItem(@PathVariable Long id) {
        UmsAdmin admin = adminService.getById(id);
        return CommonResult.success(admin);
    }

    @ApiOperation("修改指定用戶資訊帳號")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult update(@PathVariable Long id, @RequestBody UmsAdmin admin) {

        //取得組織代碼
        log.info("umsAdminParam :{}",admin.getOrgId());
        OmsOrganization omsOrganization = new OmsOrganization();
        omsOrganization =omsOrganizationService.getById(admin.getOrgId());

        log.info("omsOrganization info: {},{}" , omsOrganization.getNameSn(),omsOrganization.getName());
        admin.setOrgSn(omsOrganization.getNameSn());
        admin.setOrgName(omsOrganization.getName());

        boolean success = adminService.update(id, admin);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @ApiOperation("修改指定用戶密碼")
    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updatePassword(@Validated @RequestBody UpdateAdminPasswordParam updatePasswordParam) {
        int status = adminService.updatePassword(updatePasswordParam);
        if (status > 0) {
            return CommonResult.success(null);
        } else if (status == -1) {
            return CommonResult.failed("未通過參數檢查");
        } else if (status == -2) {
            return CommonResult.failed("無該用戶帳號");
        } else if (status == -3) {
            return CommonResult.failed("舊密碼錯誤");
        } else if (status == -4) {
            return CommonResult.passwordError("密碼強度不足");
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("删除指定用戶資訊帳號")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult delete(@PathVariable Long id) {
        boolean success = adminService.delete(id);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @ApiOperation("修改帳號狀態")
    @RequestMapping(value = "/updateStatus/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateStatus(@PathVariable Long id, @RequestParam(value = "status") Integer status) {
        UmsAdmin umsAdmin = new UmsAdmin();
        umsAdmin.setStatus(status);
        boolean success = adminService.update(id, umsAdmin);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @ApiOperation("指派角色給帳號")
    @RequestMapping(value = "/role/update", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateRole(@RequestParam("adminId") Long adminId,
                                   @RequestParam("roleIds") List<Long> roleIds) {
        int count = adminService.updateRole(adminId, roleIds);
        if (count >= 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }


    @ApiOperation("取得該用戶的角色")
    @RequestMapping(value = "/role/{adminId}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<UmsRole>> getRoleList(@PathVariable Long adminId) {
        List<UmsRole> roleList = adminService.getRoleList(adminId);
        return CommonResult.success(roleList);
    }
}
