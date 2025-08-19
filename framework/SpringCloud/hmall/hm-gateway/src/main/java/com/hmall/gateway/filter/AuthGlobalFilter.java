package com.hmall.gateway.filter;

import com.hmall.common.exception.UnauthorizedException;
import com.hmall.gateway.config.AuthProperties;
import com.hmall.gateway.util.JwtTool;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/*
    注解说明：
      1.@Component：将该过滤器类注册为Bean

    接口说明：
      1.GlobalFilter：“过滤”功能通过实现该注解的filter方法实现
      2.Ordered：过滤器的执行顺序通过该注解的getOrder方法指定
 */
@Component
@RequiredArgsConstructor
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    // 封装授权路径数据的类
    private final AuthProperties authProperties;

    // 自定义的用于生成、解析token令牌的工具
    private final JwtTool jwtTool;

    // SpringBoot提供的用于做请求路径分析的工具(这里我们使用它判断前端发送的请求路径与配置的无需做登录校验的路径是否重合)
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    /*
        参数说明：
          1. ServerWebExchange exchange：请求上下文，包含整个过滤器链内共享的数据，例如request、response、session等
          2. GatewayFilterChain chain：过滤链，当前过滤器执行完成后，要调用过滤器链中的下一个过滤器

        返回值说明：
          1. Mono<Void>：是过滤器链中下一个要执行的过滤器
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1.获取request
        ServerHttpRequest request = exchange.getRequest();
        // 2.判断是否需要做登录拦截(不需要登陆验证的请求地址统一配置在配置文件hm.auth.excludePaths属性下)，若不需要则放行
        if (isExclude(request.getPath().toString())) {
            return chain.filter(exchange);
        }
        // 3.获取token(请求头中的数据以字典的形式存在，其中存储token的key为authorization)
        String token = null;
        List<String> headers = request.getHeaders().get("authorization");
        if (headers!=null && !headers.isEmpty()) {
            token = headers.get(0);
        }
        // 4. 检验并解析token(若token异常则拦截请求，响应401)
        Long userId = null;
        try {
            userId = jwtTool.parseToken(token);
        } catch (UnauthorizedException e) {
            // token异常，拦截请求并设置响应状态码为401(登陆失败)，response.setComplete()方法同样返回Mono<Void>，表示请求到此结束，不再向后续过滤器传递
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
        // 5.传递用户信息，调用上下文对象的mutate(改变)方法，继续向下调用request(修改请求)方法，方法中构建请求头，最后调用build方法
        String userInfo = userId.toString();
        ServerWebExchange swe = exchange.mutate()
                .request(builder -> builder.header("user-info", userInfo))
                .build();
        // 6.放行
        return chain.filter(swe);
    }

    // 判断请求是否需要做登录校验(需要false，不需要true)
    private boolean isExclude(String string) {
        for (String pathPatter : authProperties.getExcludePaths()) {
            if (antPathMatcher.match(pathPatter, string)) {
                return true;
            }
        }
        return false;
    }

    // Ordered接口中的抽象方法，用于设置过滤器的执行顺序，值越小，优先级越高
    @Override
    public int getOrder() {
        return 0;
    }
}
