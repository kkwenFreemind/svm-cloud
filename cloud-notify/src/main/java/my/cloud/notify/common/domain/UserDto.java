package my.cloud.notify.common.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 登入帳號訊息
 * Created by Kevin Chang on 2022/7/6.
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private String password;
    private Integer status;
    private String clientId;
    private List<String> roles;

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", username=").append(this.username);
        sb.append(", status=").append(this.status);
        sb.append(", clientId=").append(this.clientId);
        sb.append(", role=").append(this.roles.toString());
        sb.append("]");
        return sb.toString();
    }
}
