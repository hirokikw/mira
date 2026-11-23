package com.examplecompany.mira.web;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(
            final Authentication authentication,
            @RequestParam(name = "error", required = false) final Boolean error,
            @RequestParam(name = "logout", required = false) final Boolean logout,
            final Model model) {
        if (authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {
            return "redirect:/menu";
        }
        if (Boolean.TRUE.equals(error)) {
            model.addAttribute("loginError", true);
        }
        if (Boolean.TRUE.equals(logout)) {
            model.addAttribute("logoutMessage", true);
        }
        return "login";
    }
}