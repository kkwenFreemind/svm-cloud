package my.cloud.notify.modules.nms.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author : kevin Chang
 * @since : 2022/2/24
 *
 * <P>
 *     訊息發送紀錄資訊
 * </P>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("nms_notification_category")
@ApiModel(value="NmsNotificationCategory對象", description="訊息發送分類")
public class NmsNotificationCategory {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "創建時間")
    private Date createTime;

    @ApiModelProperty(value = "發送方式")
    private String name;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "啟動狀態")
    private Integer status;

    @ApiModelProperty(value = "備註")
    private String note;
}
