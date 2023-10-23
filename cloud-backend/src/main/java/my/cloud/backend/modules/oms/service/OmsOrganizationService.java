package my.cloud.backend.modules.oms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import my.cloud.backend.modules.oms.model.OmsOrganization;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 單位組織架構管理Service
 */


public interface OmsOrganizationService extends IService<OmsOrganization> {
    /**
     * 添加組織
     */
    boolean create(OmsOrganization organization);

    /**
     * 批量删除組織
     */
    boolean delete(List<Long> ids);

    /**
     * 刪除組織
     */
    boolean delete(Long id);

    /**
     * 分頁獲得組織列表
     */
    Page<OmsOrganization> list(Long parentId, Integer pageSize, Integer pageNum);

    /**
     * 修改組織狀態
     */
    boolean updateStatus(Long id, Integer status);

    /**
     * 修改指定組織信息
     */
    boolean update(Long id, OmsOrganization organization);


    List<OmsOrganization> OrganizationLowList(Long parentId);

    /**
     * 分頁獲得組織列表
     */
    Page<OmsOrganization> list(Integer pageSize, Integer pageNum);
}
