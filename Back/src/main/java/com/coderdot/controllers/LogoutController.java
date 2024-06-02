package com.coderdot.controllers;

import com.coderdot.services.jwt.TokenOutService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/logout")
public class LogoutController {

    private final TokenOutService tokenBlacklistService;

    @Autowired
    public LogoutController(TokenOutService tokenBlacklistService) {
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @PostMapping
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (authentication != null) {
            String token = request.getHeader("Authorization").substring(7); // Remove "Bearer " prefix
            tokenBlacklistService.addTokenToBlacklist(token);

            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
