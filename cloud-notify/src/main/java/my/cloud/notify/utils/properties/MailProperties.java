package my.cloud.notify.utils.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author : kevin Chang
 * @since : 2022/3/3
 */
@Data
@ConfigurationProperties(prefix = "mail")
public class MailProperties {

    private String host;
    private Integer port;
    private String auth_enabled;
    private String starttls_enabled;
    private String user_address;
    private String user_pwd;
    private String user_display_name;

}
