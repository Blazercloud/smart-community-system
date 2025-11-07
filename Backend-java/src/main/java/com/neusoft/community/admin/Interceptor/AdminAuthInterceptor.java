package com.neusoft.community.admin.Interceptor;

import com.neusoft.community.admin.entity.Admin;
import com.neusoft.community.admin.service.AdminService;
import com.neusoft.community.common.annotation.NoAuth;
import com.neusoft.community.common.util.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 管理员身份认证拦截器
 *
 * - 校验请求头中的 Redis Token 是否有效
 * - 自动解析 adminId 并注入到请求属性
 * - 支持 @NoAuth 注解跳过认证
 *
 * @author
 */
@Slf4j
@Component
public class AdminAuthInterceptor implements HandlerInterceptor {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AdminService adminService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 非控制器方法直接放行
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;

        // 标注了 @NoAuth 的接口不拦截
        if (handlerMethod.getMethodAnnotation(NoAuth.class) != null) {
            return true;
        }

        // 从请求头获取 token
        String token = request.getHeader("Authorization");
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // token 为空则拒绝
        if (!StringUtils.hasText(token)) {
            writeUnauthorized(response, "缺少令牌，请重新登录");
            return false;
        }

        // 从 Redis 校验 token 并获取 adminId
        Long adminId = tokenService.getUserIdFromToken(token);
        if (adminId == null) {
            writeUnauthorized(response, "令牌无效或已过期");
            return false;
        }

        // 校验管理员状态
        Admin admin = adminService.getAdminById(adminId);
        if (admin == null || admin.getStatus() == 0) {
            writeUnauthorized(response, "管理员不存在或已禁用");
            return false;
        }

        // 将 adminId 写入请求，方便后续 Controller 使用
        request.setAttribute("adminId", adminId);
        return true;
    }

    /**
     * 返回 401 未授权 JSON 响应
     */
    private void writeUnauthorized(HttpServletResponse response, String message) throws Exception {
        response.setStatus(401);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"code\":401,\"message\":\"" + message + "\"}");
    }
}
