package my.cloud.backend.modules.ums.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import my.cloud.backend.common.api.CommonResult;
import my.cloud.backend.common.domain.UserDto;
import my.cloud.backend.modules.ums.dto.UmsAdminParam;
import my.cloud.backend.modules.ums.dto.UpdateAdminPasswordParam;
import my.cloud.backend.modules.ums.model.UmsAdmin;
import my.cloud.backend.modules.ums.model.UmsResource;
import my.cloud.backend.modules.ums.model.UmsRole;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 後台管理員 管理Service
 */
public interface UmsAdminService extends IService<UmsAdmin> {

    /**
     * 根據用戶名稱獲得後台管理員
     */
    UmsAdmin getAdminByUsername(String username);

    /**
     * 注册功能
     */
    UmsAdmin register(UmsAdminParam umsAdminParam);

    /**
     * 登入功能
     *
     * @param username 用戶名稱
     * @param password 密碼
     * @return 生成的JWT的token
     */
    CommonResult login(String username, String password);

    /**
     * 刷新token的功能
     *
     * @param oldToken 舊的token
     */
    String refreshToken(String oldToken);

    /**
     * 根據用戶名稱或暱稱分頁查詢用户
     */
    Page<UmsAdmin> list(String keyword, Integer pageSize, Integer pageNum);

    /**
     * 修改指定用户信息
     */
    boolean update(Long id, UmsAdmin admin);

    /**
     * 删除指定用户
     */
    boolean delete(Long id);

    /**
     * 修改用户角色關聯
     */
    @Transactional
    int updateRole(Long adminId, List<Long> roleIds);


    /**
     * 獲得用户對應角色
     */
    List<UmsRole> getRoleList(Long adminId);

    /**
     * 獲得指定用户的可訪問資源
     */
    List<UmsResource> getResourceList(Long adminId);

    /**
     * 修改密碼
     */
    int updatePassword(UpdateAdminPasswordParam updatePasswordParam);


    /**
     * 獲得用户信息
     */
    UserDto loadUserByUsername(String username);

    /**
     * 獲得當前登入後台用戶資訊
     */

}
