package my.cloud.backend.service;

import my.cloud.backend.common.api.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * Created by Kevin Chang on 2022/7/6.
 **/
@FeignClient("cloud-auth")
public interface AuthService {
    @PostMapping(value = "/oauth/token")
    CommonResult getAccessToken(@RequestParam Map<String, String> parameters);
}
