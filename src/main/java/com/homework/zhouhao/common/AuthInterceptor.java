package com.homework.zhouhao.common;

import com.homework.zhouhao.model.UserInfoVO;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    public static String AUTH_HEADER_KEY = "Authorization";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authorization = request.getHeader(AUTH_HEADER_KEY);
        if (authorization != null && !authorization.isEmpty()) {
            String decodedHeader = decodeAuthorizationHeader(authorization);
            UserInfoVO userInfoVO = JacksonUtils.fromJson(decodedHeader, UserInfoVO.class);
            if (userInfoVO != null) {
                AuthUserContext.setUserInfo(userInfoVO);
                return true;
            }
        }

        throw new BizRuntimeException(ErrorCode.UNAUTHORIZED);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        AuthUserContext.setUserInfo(null);
    }

    private String decodeAuthorizationHeader(String authorization) {
        String encodedCredentials = authorization.trim();
        return new String(Base64.getDecoder().decode(encodedCredentials));
    }
}
