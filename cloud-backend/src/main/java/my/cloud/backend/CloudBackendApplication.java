package my.cloud.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Created by Kevin Chang on 2022/7/5.
 **/
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class CloudBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(CloudBackendApplication.class,args);
    }
}
