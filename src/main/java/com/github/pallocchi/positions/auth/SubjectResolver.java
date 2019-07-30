package com.github.pallocchi.positions.auth;

import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * Convenience class to inject the subject on parameters annotated with {@link Subject}.
 *
 * <p>The subject is retrieved from security context.
 *
 * @see Subject
 * @see JwtAuthenticationFilter
 *
 * @author Pablo Pallocchi
 */
public class SubjectResolver implements HandlerMethodArgumentResolver {

    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(Subject.class) != null;
    }

    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {

        final String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return asParameterType(principal, parameter.getParameterType());
    }

    /**
     * Converts the principal to given type.
     *
     * @param principal the principal to convert
     * @param type the target type
     * @return the principal as the target type
     */
    private static Object asParameterType(String principal, Class<?> type) {

        if (ClassUtils.isAssignable(String.class, type)) {
            return principal;
        }

        if (ClassUtils.isAssignable(Integer.class, type)) {
            return Integer.valueOf(principal);
        }

        if (ClassUtils.isAssignable(Long.class, type)) {
            return Long.valueOf(principal);
        }

        throw new IllegalArgumentException("Type " + type + " is not supported as @Subject");
    }

}
