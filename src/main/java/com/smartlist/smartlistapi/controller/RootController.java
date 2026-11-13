package com.smartlist.smartlistapi.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

    @GetMapping("/")
    public Map<String, Object> index() {
        return Map.of(
            "service", "smart-list-api",
            "status", "UP",
            "version", "v1",
            "docs", "/swagger-ui/index.html",
            "health", "/api/v1/health"
        );
    }
}
