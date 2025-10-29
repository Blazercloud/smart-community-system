package com.neusoft.community.common.interceptor;

import com.neusoft.community.common.annotation.NoAuth;
import com.neusoft.community.common.util.JwtUtil;
import com.neusoft.community.user.entity.User;
import com.neusoft.community.user.service.UserService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 认证拦截器
 * 
 * @author Neusoft
 */
@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 如果不是HandlerMethod，直接放行
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;

        // 检查是否有@NoAuth注解
        if (handlerMethod.getMethodAnnotation(NoAuth.class) != null) {
            return true;
        }

        // 从请求头获取token
        String token = request.getHeader("Authorization");
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // 验证JWT token并从MySQL获取用户信息
        if (StringUtils.hasText(token) && jwtUtil.validateToken(token)) {
            Claims claims = jwtUtil.parseToken(token);
            String userIdStr = claims.getSubject();
            Long userId = Long.parseLong(userIdStr);
            
            // 从数据库验证用户
            User user = userService.getUserById(userId);
            if (user != null && user.getStatus() == 1) {
                // 将 userId 存入请求属性，后续 Controller 可通过 @RequestAttribute("userId") 获取
                request.setAttribute("userId", userId);
                return true;
            }
        }

        // token无效，返回401
        response.setStatus(401);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"code\":401,\"message\":\"Unauthorized\"}");
        return false;
    }
}

