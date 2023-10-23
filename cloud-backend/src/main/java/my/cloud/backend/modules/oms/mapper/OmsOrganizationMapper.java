package my.cloud.backend.modules.oms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import my.cloud.backend.modules.oms.model.OmsOrganization;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 單位組織 Mapper 接口
 * </p>
 */
public interface OmsOrganizationMapper extends BaseMapper<OmsOrganization> {

    /**
     * 獲取對應的公司下層組織
     */
    List<OmsOrganization> getOrgListByParentId(@Param("parentId") Long parentId);


}
