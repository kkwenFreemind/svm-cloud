package my.cloud.notify;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Created by Kevin Chang on 2022/7/7.
 **/
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class CloudNotifyApplication {
    public static void main(String[] args) {
        SpringApplication.run(CloudNotifyApplication.class,args);
    }
}
