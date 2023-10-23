package my.cloud.notify.modules.nms.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import my.cloud.notify.modules.nms.model.NmsNotificationLog;

import java.io.Serializable;
import java.util.Date;

/**
 * @author : kevin Chang
 * @since : 2022/3/9
 */
@Data
public class NmsNotificationVO extends NmsNotificationLog {

//    @TableId(value = "id", type = IdType.AUTO)
//    private Long id;
//
//    @ApiModelProperty(value = "創建時間")
//    private Date createTime;
//
//    @ApiModelProperty(value = "回覆結果時間")
//    private Date responseTime;
//
//    @ApiModelProperty(value = "發送狀態 0->未發送；1->發送成功;2->發送失敗")
//    private Integer status;
//
//    @ApiModelProperty(value = "發送內容")
//    private String context;

    @ApiModelProperty(value = "發送方式 1->TeamPlus; 2->SMS; 3->eMail")
    private String name;

//    @ApiModelProperty(value = "發送方式 1->TeamPlus; 2->SMS; 3->eMail")
//    private Integer categoryId;
//
//    @ApiModelProperty(value = "發送對象，例如：門號/Email或群組代碼")
//    private String  sendTo;


}
