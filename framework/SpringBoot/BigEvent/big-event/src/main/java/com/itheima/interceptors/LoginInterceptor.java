package com.itheima.interceptors;

import com.itheima.utils.JwtUtil;
import com.itheima.utils.ThreadLocalUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

// 登录拦截器(实现HandlerInterceptor接口)
@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private StringRedisTemplate redisTemplate;

    // 执行Controller方法之前(请求开始之前)
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 令牌验证
        /* 请求头中存在key:Authorization,value:token的键值对，通过HttpServletRequest的getHeader方法获取指定key的value(token)，若key不存在方法返回null */
        String token = request.getHeader("Authorization");
        try {
            //检查当前token是否合法(redisTemplate.opsForValue()是用于获取Redis操作缓存的接口)
            String redisToken = redisTemplate.opsForValue().get(token);
            if (redisToken == null) {
                throw new RuntimeException();
            }
            // 通过传入token，获取封存在token令牌中的业务数据(若token为null，方法报错)
            Map<String, Object> claims = JwtUtil.parseToken(token);
            // 把业务数据存储到ThreadLocal中
            ThreadLocalUtil.set(claims);
            return true; // 放行
        } catch (Exception e) {
            response.setStatus(401);
            return false; // 拦截
        }
    }

    // 执行Controller方法之后(请求结束之后)
    /* 由于线程池的使用，在每次请求结束之后都应释放线程池中的数据，来避免当前请求中的数据泄露给后续请求 */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
            throws Exception {
        // 清空ThreadLocal中的数据
        ThreadLocalUtil.remove();
    }
}
