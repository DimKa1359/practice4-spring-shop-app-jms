package com.example.springshopapp.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class XmlResponseInterceptor implements HandlerInterceptor {

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {

        String acceptHeader = request.getHeader("Accept");
        String contentType = response.getContentType();

        // Проверка, что ответ XML и запрос не от AJAX/API клиента
        if (contentType != null && contentType.contains("application/xml")
                && (acceptHeader == null || acceptHeader.contains("application/xml"))) {

            // Ссылка на XSLT
            response.setHeader("X-XSLT-Stylesheet", "/xsl/products.xsl");
        }
    }
}