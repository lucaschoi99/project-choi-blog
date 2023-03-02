package com.projectchoi.api.config;

import com.projectchoi.api.annotation.Auth;
import com.projectchoi.api.config.data.UserSession;
import com.projectchoi.api.domain.Session;
import com.projectchoi.api.exception.UnauthorizedUser;
import com.projectchoi.api.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@Slf4j
public class AuthResolver implements HandlerMethodArgumentResolver {

    private final SessionRepository sessionRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasParameterAnnotation = parameter.hasParameterAnnotation(Auth.class);
        boolean hasUserSessionType = parameter.getParameterType().equals(UserSession.class);

        return hasParameterAnnotation && hasUserSessionType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        if (servletRequest == null) {
            log.error("servlet request NULL");
            throw new UnauthorizedUser();
        }
        Cookie[] cookies = servletRequest.getCookies();
        if (cookies.length == 0) {
            log.error("No cookie");
            throw new UnauthorizedUser();
        }

        String accessToken = cookies[0].getValue();

        // 데이터베이스 사용자 확인 작업
        Session session = sessionRepository.findByAccessToken(accessToken)
                .orElseThrow(UnauthorizedUser::new);

        return new UserSession(session.getUsers().getId());
    }
}
