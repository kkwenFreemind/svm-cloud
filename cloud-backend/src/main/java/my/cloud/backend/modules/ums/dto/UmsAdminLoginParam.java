package my.cloud.backend.modules.ums.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;

/**
 * 用户登入參數
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UmsAdminLoginParam {
    @NotEmpty
    @ApiModelProperty(value = "用户名稱",required = true)
    private String username;
    @NotEmpty
    @ApiModelProperty(value = "密碼",required = true)
    private String password;
}
