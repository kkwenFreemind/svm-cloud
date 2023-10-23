package my.cloud.notify.modules.nms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import my.cloud.notify.modules.nms.dto.NmsNotificationCategoryParam;
import my.cloud.notify.modules.nms.model.NmsNotificationCategory;
import my.cloud.notify.modules.nms.model.NmsNotificationLog;



import java.util.List;

/**
 * @author : kevin Chang
 * @since : 2022/3/2
 *
 * 發送類型Service
 */
public interface NmsNotificationLogCategoryService extends IService<NmsNotificationCategory> {
    /**
     * 獲得所有發送分類
     */
    List<NmsNotificationCategory> listAll();

    /**
     * 分頁發送分類
     */
    Page<NmsNotificationCategory> getCateList(String keyword, Integer pageSize, Integer pageNum);


    /**
     * 創建發送分類資訊
     */
    NmsNotificationCategory create(NmsNotificationCategoryParam nmsNotificationCategoryParam);

    /**
     * 修改分類啟用狀態
     */
    boolean updateStatus(Long id, Integer status);

    /**
     * 修改指定發送分類訊息
     */
    boolean update(Long id, NmsNotificationCategory nmsNotificationCategory);

    /**
     * 創建發送分類訊息
     */
    boolean create(NmsNotificationCategory nmsNotificationCategory);
}
