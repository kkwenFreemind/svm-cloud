package my.cloud.notify.modules.nms.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

/**
 * @author : kevin Chang
 * @since : 2022/3/3
 *
 * 發送方式參數
 */
@Getter
@Setter
public class NmsNotificationCategoryParam {

    @NotEmpty
    @ApiModelProperty(value = "發送方式", required = true)
    private String name;

    @ApiModelProperty(value = "備註")
    private String note;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "啟動狀態")
    private Integer status;
}
