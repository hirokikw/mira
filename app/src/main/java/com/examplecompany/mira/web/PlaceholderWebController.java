package com.examplecompany.mira.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * メニュー先の未実装画面用プレースホルダー（wb01 / wc01 実装まで）。
 */
@Controller
public class PlaceholderWebController {

    @GetMapping("/users/registration")
    public String userRegistration(final Model model) {
        model.addAttribute("title", "ユーザー登録");
        model.addAttribute("message", "ユーザー登録画面は未実装です。");
        return "placeholder";
    }

    @GetMapping("/users")
    public String userList(final Model model) {
        model.addAttribute("title", "ユーザー一覧");
        model.addAttribute("message", "ユーザー一覧画面は未実装です。");
        return "placeholder";
    }
}