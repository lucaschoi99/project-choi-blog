package com.projectchoi.api.config;

import com.projectchoi.api.config.data.UserSession;
import com.projectchoi.api.exception.UnauthorizedUser;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class AuthResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserSession.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String accessToken = webRequest.getParameter("accessToken");
        if (accessToken == null || accessToken.equals("")) {
            throw new UnauthorizedUser();
        }
        UserSession userSession = new UserSession();
        userSession.name = accessToken;
        return userSession;
    }
}
