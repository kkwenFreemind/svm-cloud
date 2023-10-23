package my.cloud.backend.modules.ums.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import my.cloud.backend.modules.ums.model.UmsMenu;

import java.util.List;

/**
 * 後台菜單節點封裝
 */
@Getter
@Setter
public class UmsMenuNode extends UmsMenu {
    @ApiModelProperty(value = "子集菜單")
    private List<UmsMenuNode> children;
}
