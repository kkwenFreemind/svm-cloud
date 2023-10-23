package my.cloud.gateway.authorization;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
//import cn.hutool.json.JSONUtil;
//import com.nimbusds.jose.JWSObject;
//import lombok.extern.slf4j.Slf4j;
import my.cloud.gateway.common.constant.AuthConstant;
//import my.cloud.gateway.common.domain.UserDto;
import my.cloud.gateway.config.IgnoreUrlsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 主要思路是：
 * step1: 獲取當前訪問路徑，
 * step2: 當前訪問路徑是否在白名單內
 * step3: 不屬於白名單，則檢查token，若不存在則校驗失敗
 * step4: 從redis取出Resource & Role 關聯資料，並放置在Map Object中
 * step5: 取出token擁有的角色(authorities)
 *
 * Created by macro on 2020/6/19.
 * Edit by Kevin on 2022/07/01
 */
@Component
public class AuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationManager.class);

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private IgnoreUrlsConfig ignoreUrlsConfig;

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> mono, AuthorizationContext authorizationContext) {


        ServerHttpRequest request = authorizationContext.getExchange().getRequest();
        URI uri = request.getURI();
        LOGGER.info("當前請求路徑 --> "+uri.getPath());

        PathMatcher pathMatcher = new AntPathMatcher();
        //白名单路径直接放行
        List<String> ignoreUrls = ignoreUrlsConfig.getUrls();
        for (String ignoreUrl : ignoreUrls) {
            if (pathMatcher.match(ignoreUrl, uri.getPath())) {
                LOGGER.info("白名單 Pass-->"+uri.getPath());
                return Mono.just(new AuthorizationDecision(true));
            }
        }
        //HttpMethod.OPTIONS Pass
        if(request.getMethod()==HttpMethod.OPTIONS){
            LOGGER.info("HttpMethod.OPTIONS Pass-->"+uri.getPath());
            return Mono.just(new AuthorizationDecision(true));
        }

        //Token 檢查
        try {
            //AuthConstant.JWT_TOKEN_HEADER = "Authorization"
            String token = request.getHeaders().getFirst(AuthConstant.JWT_TOKEN_HEADER);

            if(StrUtil.isEmpty(token) ){
                LOGGER.info("Error AuthConstant.JWT_TOKEN_HEADER --> "+uri.getPath());
                return Mono.just(new AuthorizationDecision(false));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Mono.just(new AuthorizationDecision(false));
        }

        //從redis取出Resource & Role 關聯資料，並放置在Map Object中
        Map<Object, Object> resourceRolesMap = redisTemplate.opsForHash().entries(AuthConstant.RESOURCE_ROLES_MAP_KEY);
        Iterator<Object> iterator = resourceRolesMap.keySet().iterator();
        List<String> authorities = new ArrayList<>();
        while (iterator.hasNext()) {
            String pattern = (String) iterator.next();
            if (pathMatcher.match(pattern, uri.getPath())) {
                authorities.addAll(Convert.toList(String.class, resourceRolesMap.get(pattern)));
            }
        }
        //AuthConstant.AUTHORITY_PREFIX == "ROLE_"
        authorities = authorities.stream().map(i -> i = AuthConstant.AUTHORITY_PREFIX + i).collect(Collectors.toList());
        LOGGER.info("authorities --> " + authorities.toString());

        return mono
                .filter(Authentication::isAuthenticated)
                .flatMapIterable(Authentication::getAuthorities)
                .map(GrantedAuthority::getAuthority)
                .any(authorities::contains)
                .map(AuthorizationDecision::new)
                .defaultIfEmpty(new AuthorizationDecision(false));
    }

}
