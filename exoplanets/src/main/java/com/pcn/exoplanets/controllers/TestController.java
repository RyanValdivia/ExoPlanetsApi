package com.pcn.exoplanets.controllers;

import com.pcn.exoplanets.models.user.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/test")
    public String test() {
        return "Hola tonoto";
    }

    @GetMapping ("/image")
    public String image(@AuthenticationPrincipal User user) {
        return user.getImageUrl();
    }
}
