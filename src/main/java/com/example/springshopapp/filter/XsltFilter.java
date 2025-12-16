package com.example.springshopapp.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

@Component
public class XsltFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Определение, нужно ли добавлять XSLT
        boolean shouldAddXslt = shouldAddXsltProcessingInstruction(httpRequest);

        if (shouldAddXslt) {
            // Обертывание ответа для модификации
            ContentCachingResponseWrapper responseWrapper =
                    new ContentCachingResponseWrapper(httpResponse);

            chain.doFilter(request, responseWrapper);

            // Получение контента
            byte[] content = responseWrapper.getContentAsByteArray();
            String contentStr = new String(content, responseWrapper.getCharacterEncoding());

            // Добавление инструкции обработки XSLT
            if (contentStr.contains("<?xml")) {
                contentStr = contentStr.replaceFirst(
                        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>",
                        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                                "<?xml-stylesheet type=\"text/xsl\" href=\"/xsl/products.xsl\"?>"
                );
            }

            responseWrapper.resetBuffer();
            responseWrapper.getWriter().write(contentStr);
            responseWrapper.copyBodyToResponse();

        } else {
            chain.doFilter(request, response);
        }
    }

    private boolean shouldAddXsltProcessingInstruction(HttpServletRequest request) {
        String path = request.getRequestURI();
        String accept = request.getHeader("Accept");

        if (path.startsWith("/api/")) {
            if (accept != null && accept.contains("application/xml")) {
                return true;
            }

            String userAgent = request.getHeader("User-Agent");
            if (userAgent != null && userAgent.contains("Mozilla")) {
                return true;
            }
        }
        return false;
    }
}