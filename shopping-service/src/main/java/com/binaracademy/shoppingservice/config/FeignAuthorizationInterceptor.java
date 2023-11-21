package com.binaracademy.shoppingservice.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class FeignAuthorizationInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        String authorizationHeaderValue = getAuthorizationHeaderValue();

        if (authorizationHeaderValue != null) {
            template.header("Authorization", authorizationHeaderValue);
        }
    }

    private String getAuthorizationHeaderValue() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            return request.getHeader("Authorization");
        }

        return null;
    }
}
