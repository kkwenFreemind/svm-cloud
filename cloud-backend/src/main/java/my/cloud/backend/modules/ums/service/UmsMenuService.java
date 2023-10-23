package my.cloud.backend.modules.ums.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import my.cloud.backend.modules.ums.dto.UmsMenuNode;
import my.cloud.backend.modules.ums.model.UmsMenu;

import java.util.List;


/**
 * 後台菜單管理Service
 */
public interface UmsMenuService extends IService<UmsMenu> {
    /**
     * 創建後台菜單
     */
    boolean create(UmsMenu umsMenu);

    /**
     * 修改後台菜單
     */
    boolean update(Long id, UmsMenu umsMenu);

    /**
     * 分頁查詢後台菜單
     */
    Page<UmsMenu> list(Long parentId, Integer pageSize, Integer pageNum);

    /**
     * 樹狀結構返回所有菜單列表
     */
    List<UmsMenuNode> treeList();

    /**
     * 修改菜單顯示狀態
     */
    boolean updateHidden(Long id, Integer hidden);
}
