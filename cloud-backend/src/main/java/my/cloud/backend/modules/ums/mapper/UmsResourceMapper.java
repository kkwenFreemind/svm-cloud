package my.cloud.backend.modules.ums.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import my.cloud.backend.modules.ums.model.UmsResource;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 後台資源 Mapper 接口
 * </p>
 *
 */
public interface UmsResourceMapper extends BaseMapper<UmsResource> {

    /**
     * 獲取用户所有可訪問資源
     */
    List<UmsResource> getResourceList(@Param("adminId") Long adminId);

    /**
     * 根據角色ID獲取資源
     */
    List<UmsResource> getResourceListByRoleId(@Param("roleId") Long roleId);

}
