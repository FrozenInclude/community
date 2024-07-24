package com.bcsd.community.controller.page;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Controller
public class InfoPage {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/info")
    public String getMemberInfo(HttpSession session, Model model) {
        Map<String, Object> loginUser = (Map<String, Object>) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("username", loginUser.get("username"));
        model.addAttribute("email", loginUser.get("email"));
        String joinDate = (String) loginUser.get("createdAt");
        model.addAttribute("joinDate", joinDate);

        return "info_layout";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @PostMapping("/delete")
    public String deleteMember(HttpSession session, Model model) {
        Map<String, Object> loginUser = (Map<String, Object>) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login";
        }

        String deleteUrl = "http://localhost:8080/api/member";
        try {
            restTemplate.delete(deleteUrl);
            session.invalidate();
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("error", "회원탈퇴에 실패하였습니다.");
            return "info_layout";
        }
    }
}
