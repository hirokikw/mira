package com.examplecompany.mira.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MenuController {

    @GetMapping("/menu")
    public String menu(@AuthenticationPrincipal final UserDetails userDetails, final Model model) {
        model.addAttribute("loginId", userDetails != null ? userDetails.getUsername() : "");
        return "menu";
    }
}