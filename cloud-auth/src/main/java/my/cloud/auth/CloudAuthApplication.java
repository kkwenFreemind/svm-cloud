package my.cloud.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "my.cloud")
public class CloudAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(CloudAuthApplication.class,args);
    }
}
