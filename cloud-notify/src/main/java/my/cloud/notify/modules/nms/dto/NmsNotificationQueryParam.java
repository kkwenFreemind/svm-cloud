package my.cloud.notify.modules.nms.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Notification 查询参数
 * @author : kevin Chang
 * @since : 2022/3/1
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class NmsNotificationQueryParam {

    @ApiModelProperty("發送結果")
    private Integer publishStatus;

    @ApiModelProperty("發送訊息模糊關鍵字")
    private String keyword;

}
