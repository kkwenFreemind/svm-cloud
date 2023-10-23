package my.cloud.backend.modules.ums.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import lombok.extern.slf4j.Slf4j;
//import my.cloud.backend.common.exception.Asserts;
//import my.cloud.backend.domain.AdminUserDetails;
import my.cloud.backend.common.api.CommonResult;
import my.cloud.backend.common.api.ResultCode;
import my.cloud.backend.common.constant.AuthConstant;
import my.cloud.backend.common.domain.UserDto;
import my.cloud.backend.common.exception.Asserts;
import my.cloud.backend.modules.ums.dto.UmsAdminParam;
import my.cloud.backend.modules.ums.dto.UpdateAdminPasswordParam;
import my.cloud.backend.modules.ums.mapper.*;
import my.cloud.backend.modules.ums.model.*;
import my.cloud.backend.modules.ums.service.UmsAdminRoleRelationService;
import my.cloud.backend.modules.ums.service.UmsAdminService;

//import my.cloud.backend.security.util.JwtTokenUtil;
import my.cloud.backend.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 後台管理員管理Service實現類
 */
@Service
@Slf4j
public class UmsAdminServiceImpl extends ServiceImpl<UmsAdminMapper, UmsAdmin> implements UmsAdminService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UmsAdminServiceImpl.class);

    @Autowired
    private AuthService authService;
    @Autowired
    private UmsAdminLoginLogMapper loginLogMapper;
    //    @Autowired
//    private UmsAdminCacheService adminCacheService;
    @Autowired
    private UmsAdminRoleRelationService adminRoleRelationService;
    @Autowired
    private UmsRoleMapper roleMapper;
    @Autowired
    private UmsResourceMapper resourceMapper;

    @Autowired
    private HttpServletRequest request;

    @Override
    public UmsAdmin getAdminByUsername(String username) {
//        UmsAdmin admin = adminCacheService.getAdmin(username);
//        if(admin!=null) return  admin;
        QueryWrapper<UmsAdmin> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UmsAdmin::getUsername, username);
        List<UmsAdmin> adminList = list(wrapper);
        if (adminList != null && adminList.size() > 0) {
            UmsAdmin admin = adminList.get(0);
//            adminCacheService.setAdmin(admin);
            return admin;
        }
        return null;
    }

    @Override
    public UmsAdmin register(UmsAdminParam umsAdminParam) {
        UmsAdmin umsAdmin = new UmsAdmin();
        BeanUtils.copyProperties(umsAdminParam, umsAdmin);
        umsAdmin.setCreateTime(new Date());
        umsAdmin.setStatus(1);
        //查詢是否有相同用户名的用户
        QueryWrapper<UmsAdmin> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UmsAdmin::getUsername, umsAdmin.getUsername());
        List<UmsAdmin> umsAdminList = list(wrapper);
        if (umsAdminList.size() > 0) {
            return null;
        }
        //将密碼进行加密操作
        String encodePassword = "";
        umsAdmin.setPassword(encodePassword);
        baseMapper.insert(umsAdmin);
        return umsAdmin;
    }

    @Override
    public CommonResult login(String username, String password) {

        //帳號密碼檢查
        if (StrUtil.isEmpty(username) || StrUtil.isEmpty(password)) {
            Asserts.fail("用戶名稱及密碼不可為空值！");
        }

        //將Json Request Body 轉換成 request parameters，
        Map<String, String> params = new HashMap<>();
        params.put("client_id", AuthConstant.ADMIN_CLIENT_ID);
        params.put("client_secret", "123456");
        params.put("grant_type", "password");
        params.put("username", username);
        params.put("password", password);

        //呼叫OAuth API
        CommonResult restResult = authService.getAccessToken(params);
        if (ResultCode.SUCCESS.getCode() == restResult.getCode() && restResult.getData() != null) {
            updateLoginTimeByUsername(username);
            insertLoginLog(username);
        }
        log.info(restResult.toString());
        return restResult;
    }

    /**
     * 添加登入紀錄
     *
     * @param username 用户名
     */
    private void insertLoginLog(String username) {
        UmsAdmin admin = getAdminByUsername(username);
        if (admin == null) return;
        UmsAdminLoginLog loginLog = new UmsAdminLoginLog();
        loginLog.setAdminId(admin.getId());
        loginLog.setCreateTime(new Date());
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        loginLog.setIp(request.getRemoteAddr());
        loginLogMapper.insert(loginLog);

        log.info("update user: {} login_time", username);
        updateLoginTimeByUsername(username);
    }

    /**
     * 根據用户名修改登入時间
     */
    private void updateLoginTimeByUsername(String username) {
        UmsAdmin record = new UmsAdmin();
        record.setLoginTime(new Date());
        QueryWrapper<UmsAdmin> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UmsAdmin::getUsername, username);
        update(record, wrapper);
    }

    @Override
    public String refreshToken(String oldToken) {
        return null;

    }

    @Override
    public Page<UmsAdmin> list(String keyword, Integer pageSize, Integer pageNum) {
        Page<UmsAdmin> page = new Page<>(pageNum, pageSize);
        QueryWrapper<UmsAdmin> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<UmsAdmin> lambda = wrapper.lambda();
        if (StrUtil.isNotEmpty(keyword)) {
            lambda.like(UmsAdmin::getUsername, keyword);
            lambda.or().like(UmsAdmin::getNickName, keyword);
        }
        return page(page, wrapper);
    }

    @Override
    public boolean update(Long id, UmsAdmin admin) {
        admin.setId(id);
        UmsAdmin rawAdmin = getById(id);
        if (rawAdmin.getPassword().equals(admin.getPassword())) {
            //與原加密密碼相同的不需要修改
            admin.setPassword(null);
        } else {
            //與原加密密碼不同的需要加密修改
            if (StrUtil.isEmpty(admin.getPassword())) {
                admin.setPassword(null);
            } else {
                //admin.setPassword(passwordEncoder.encode(admin.getPassword()));
            }
        }
        boolean success = updateById(admin);
//        adminCacheService.delAdmin(id);
        return success;
    }

    @Override
    public boolean delete(Long id) {
//        adminCacheService.delAdmin(id);
        boolean success = removeById(id);
//        adminCacheService.delResourceList(id);
        return success;
    }

    @Override
    public int updateRole(Long adminId, List<Long> roleIds) {
        int count = roleIds == null ? 0 : roleIds.size();
        //先删除原来的關聯
        QueryWrapper<UmsAdminRoleRelation> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UmsAdminRoleRelation::getAdminId, adminId);
        adminRoleRelationService.remove(wrapper);
        //建立新關聯
        if (!CollectionUtils.isEmpty(roleIds)) {
            List<UmsAdminRoleRelation> list = new ArrayList<>();
            for (Long roleId : roleIds) {
                UmsAdminRoleRelation roleRelation = new UmsAdminRoleRelation();
                roleRelation.setAdminId(adminId);
                roleRelation.setRoleId(roleId);
                list.add(roleRelation);
            }
            adminRoleRelationService.saveBatch(list);
        }
//        adminCacheService.delResourceList(adminId);
        return count;
    }

    @Override
    public List<UmsRole> getRoleList(Long adminId) {
        return roleMapper.getRoleList(adminId);
    }


    @Override
    public List<UmsResource> getResourceList(Long adminId) {
//        List<UmsResource> resourceList = adminCacheService.getResourceList(adminId);
//        if(CollUtil.isNotEmpty(resourceList)){
//            return  resourceList;
//        }
        List<UmsResource> resourceList = resourceMapper.getResourceList(adminId);
//        if(CollUtil.isNotEmpty(resourceList)){
//            adminCacheService.setResourceList(adminId,resourceList);
//        }
        return resourceList;
    }

    @Override
    public int updatePassword(UpdateAdminPasswordParam param) {
        if (StrUtil.isEmpty(param.getUsername())
                || StrUtil.isEmpty(param.getOldPassword())
                || StrUtil.isEmpty(param.getNewPassword())) {
            return -1;
        }
        QueryWrapper<UmsAdmin> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UmsAdmin::getUsername, param.getUsername());
        List<UmsAdmin> adminList = list(wrapper);
        if (CollUtil.isEmpty(adminList)) {
            return -2;
        }
        UmsAdmin umsAdmin = adminList.get(0);
//        if(!passwordEncoder.matches(param.getOldPassword(),umsAdmin.getPassword())){
//            return -3;
//        }

        /**
         * 新增密碼強度檢查
         */
        if (param.getNewPassword().length() < 8 || param.getNewPassword().length() > 13) {
            log.info("new Password :{}", param.getNewPassword());
            return -4;
        }

//        umsAdmin.setPassword(passwordEncoder.encode(param.getNewPassword()));
        updateById(umsAdmin);
//        adminCacheService.delAdmin(umsAdmin.getId());
        return 1;
    }

    @Override
    public UserDto loadUserByUsername(String username) {
        log.info("loadUserByUsername --> " + username);
        UmsAdmin admin = getAdminByUsername(username);
        if (admin != null) {
            List<UmsRole> roleList = getRoleList(admin.getId());
            UserDto userDTO = new UserDto();
            BeanUtils.copyProperties(admin,userDTO);
            if(CollUtil.isNotEmpty(roleList)){
                List<String> roleStrList = roleList.stream().map(item -> item.getId() + "_" + item.getName()).collect(Collectors.toList());
                userDTO.setRoles(roleStrList);
            }
            log.info("userDTO -->" + userDTO.toString());
            return userDTO;
        }
        return null;
    }



//    @Override
//    public UserDetails loadUserByUsername(String username){
//        //獲得用户信息
//        UmsAdmin admin = getAdminByUsername(username);
//        if (admin != null) {
//            List<UmsResource> resourceList = getResourceList(admin.getId());
//            return new AdminUserDetails(admin,resourceList);
//        }
//        throw new UsernameNotFoundException("用户名或密碼錯誤");
//    }
}
