package my.cloud.backend.modules.oms.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 設備資訊
 * </p>
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("oms_organization")
@ApiModel(value="OmsOrganization對象", description="組織資訊")
public class OmsOrganization implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "上層組織代碼")
    private Long parentId;

    @ApiModelProperty(value = "組織編號")
    private Long nameSn;

    @ApiModelProperty(value = "組織名稱")
    private String name;

    @ApiModelProperty(value = "層級")
    private Integer level;

    @ApiModelProperty(value = "狀態")
    private Integer status;

    @ApiModelProperty(value = "創建時間")
    private Date createTime;

    @ApiModelProperty(value = "排序")
    private Integer sort;

}
