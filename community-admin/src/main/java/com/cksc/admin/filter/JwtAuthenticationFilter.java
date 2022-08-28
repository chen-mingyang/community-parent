package com.cksc.admin.filter;


import com.cksc.admin.utils.JwtUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import static com.cksc.admin.utils.JwtUtils.ROLE;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/3/21 20:01
 * @description JWT filter(拦截器/过滤器)
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter{

    private static final PathMatcher pathmatcher = new AntPathMatcher();

    //哪些请求需要进行安全校验 把用户角色注入Spring Security 哪些请求需要角色验证，就加入进来
    private String[] protectUrlPattern = {"/**/manage/**", "/**/member/**", "/**/auth/**","/**/user/**","/**/team/**","/**/article/**","/**/problem/**","/**/tag/**","/**/category/**","/**/comment/**"};

    public JwtAuthenticationFilter() {

    }


    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
    //是不是可以在这里做多种方式登录呢
        try {
            if (isProtectedUrl(httpServletRequest)) {
                //校验Token 得到B部分playload 如用户ID、用户角色键值对
                Map<String, Object> claims = JwtUtils.validateTokenAndGetClaims(httpServletRequest);
                String role = String.valueOf(claims.get(ROLE));
                String userid = String.valueOf(claims.get("userid"));

                //最关键的部分就是这里, 往Spring Security直接注入 UsernamePasswordAuthenticationToken携带B部分信息
                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                        userid, null, Arrays.asList(() -> role)
                ));

            }
        } catch (Exception e) {
            e.printStackTrace();
            //对于异常处理，使用@ControllerAdvice是不行的，这个是Filter，在这里抛的异常还没有到DispatcherServlet，无法处理。所以Filter要自己做异常处理
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
            return;
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);


    }

    //否是保护连接
    private boolean isProtectedUrl(HttpServletRequest request) {
        boolean flag = false;
        for (int i = 0; i < protectUrlPattern.length; i++) {
            if (pathmatcher.match(protectUrlPattern[i], request.getServletPath())) {
                return true;
            }
        }
        return false;
    }
}
