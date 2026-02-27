package com.devops.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public String home() {
        return "DevOps CI/CD with ArgoCD 🚀 and check1";
    }

    @GetMapping("/health")
    public String health() {
        return "UP";
    }
}