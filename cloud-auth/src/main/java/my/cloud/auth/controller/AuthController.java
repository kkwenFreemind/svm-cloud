package my.cloud.auth.controller;

import my.cloud.auth.domain.Oauth2TokenDto;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiImplicitParam;
//import io.swagger.annotations.ApiImplicitParams;
//import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import my.cloud.auth.common.api.CommonResult;
import my.cloud.auth.common.constant.AuthConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
//import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;
import java.util.Map;

/**
 * 自定义Oauth2获取令牌接口
 * Created by macro on 2020/7/17.
 */
@RestController
@Slf4j
//@Api(tags = "AuthController", description = "认证中心登录认证")
@RequestMapping("/oauth")
public class AuthController {

    @Autowired
    private TokenEndpoint tokenEndpoint;

//    @ApiOperation("Oauth2获取token")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "grant_type", value = "授权模式", required = true),
//            @ApiImplicitParam(name = "client_id", value = "Oauth2客户端ID", required = true),
//            @ApiImplicitParam(name = "client_secret", value = "Oauth2客户端秘钥", required = true),
//            @ApiImplicitParam(name = "refresh_token", value = "刷新token"),
//            @ApiImplicitParam(name = "username", value = "登录用户名"),
//            @ApiImplicitParam(name = "password", value = "登录密码")
//    })
    @RequestMapping(value = "/token", method = RequestMethod.POST)
    public CommonResult<Oauth2TokenDto> postAccessToken( Principal principal,  @RequestParam Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {

        String username = null;
        for(String key : parameters.keySet()){
            String value  = parameters.get(key);
            log.info("/oauth/token --> "  + key +" " + value);
        }

        OAuth2AccessToken oAuth2AccessToken = tokenEndpoint.postAccessToken(principal, parameters).getBody();

        //https://blog.csdn.net/liman65727/article/details/119296307
        //獲得token的請求
        //step1: TokenEndPoint --> 處理/oauth/token的controller
        //step2: ClientDetailsService --> 就是用來根據請求中傳遞進來的clientId，去讀取客戶端的相關信息，然後封裝到ClientDetails中
        //step3: TokenGranter -->這個接口定義了根據不同的認證類型，生成token的方式，其源碼只有一行
        //step4: 構造OAuth2Authentication --> 構造OAuth2Authentication，不同的授權方式也會有不同，畢竟認證的方式不同。以密碼授權模式為例。如果是密碼的授權模式，則TokenGranter中getAccessToken中獲取OAuth2Authentication對象的方法
        //step5: token的存儲與創建 --> 在創建完成了OAuth2Authentication之後，就是創建令牌了，spring-security-oauth提供了一個默認的tokenService的實現，名稱為DefaultTokenServices

        Oauth2TokenDto oauth2TokenDto = Oauth2TokenDto.builder()
                .token(oAuth2AccessToken.getValue())
                .refreshToken(oAuth2AccessToken.getRefreshToken().getValue())
                .expiresIn(oAuth2AccessToken.getExpiresIn())
                .tokenHead(AuthConstant.JWT_TOKEN_PREFIX).build();
        //log.info("/oauth/token (Resp)--> "+ oAuth2AccessToken.toString());
        return CommonResult.success(oauth2TokenDto);
    }
}
