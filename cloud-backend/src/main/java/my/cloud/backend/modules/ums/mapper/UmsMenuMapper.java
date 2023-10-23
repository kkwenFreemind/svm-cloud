package my.cloud.backend.modules.ums.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import my.cloud.backend.modules.ums.model.UmsMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 後台菜單 Mapper 接口
 * </p>
 */
public interface UmsMenuMapper extends BaseMapper<UmsMenu> {

    /**
     * 根據用户ID獲取菜單
     */
    List<UmsMenu> getMenuList(@Param("adminId") Long adminId);
    /**
     * 根據角色ID獲取菜單
     */
    List<UmsMenu> getMenuListByRoleId(@Param("roleId") Long roleId);

}
