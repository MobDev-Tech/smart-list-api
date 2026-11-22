package com.smartlist.smartlistapi.config;

import com.smartlist.smartlistapi.common.dto.ApiErrorResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Component
public class ApiKeyFilter extends OncePerRequestFilter {

    public static final String API_KEY_HEADER = "X-API-Key";

    private static final String API_PATH_PREFIX = "/api/v1/";
    private static final String HEALTH_PATH = "/api/v1/health";

    private final String expectedApiKey;
    private final ObjectMapper objectMapper;

    public ApiKeyFilter(
            @Value("${app.security.api-key:}") String expectedApiKey,
            ObjectMapper objectMapper) {
        Assert.hasText(expectedApiKey, "The API_KEY environment variable must be configured");
        this.expectedApiKey = expectedApiKey;
        this.objectMapper = objectMapper;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String contextPath = request.getContextPath();
        String path = request.getRequestURI().substring(contextPath.length());
        return !path.startsWith(API_PATH_PREFIX) || HEALTH_PATH.equals(path);
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String suppliedApiKey = request.getHeader(API_KEY_HEADER);
        if (!isValidApiKey(suppliedApiKey)) {
            writeUnauthorizedResponse(response);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean isValidApiKey(String suppliedApiKey) {
        if (suppliedApiKey == null || suppliedApiKey.isBlank()) {
            return false;
        }

        return MessageDigest.isEqual(
                expectedApiKey.getBytes(StandardCharsets.UTF_8),
                suppliedApiKey.getBytes(StandardCharsets.UTF_8));
    }

    private void writeUnauthorizedResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        objectMapper.writeValue(response.getOutputStream(),
                new ApiErrorResponse(HttpStatus.UNAUTHORIZED.value(), "Chave de API ausente ou inválida."));
    }
}
