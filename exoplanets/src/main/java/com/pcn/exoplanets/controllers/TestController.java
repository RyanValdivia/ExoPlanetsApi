package com.pcn.exoplanets.controllers;

import com.pcn.exoplanets.models.user.User;
import com.pcn.exoplanets.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping ("/test")
@RequiredArgsConstructor
public class TestController {
    private final UserRepository userRepository;

    @GetMapping("/test")
    public String test() {
        return "Hola tonoto";
    }

    @GetMapping ("/delete")
    public void delete () {
        userRepository.deleteAll();
    }

    @GetMapping ("/image")
    public String image(@AuthenticationPrincipal User user) {
        return user.getImageUrl();
    }
}
