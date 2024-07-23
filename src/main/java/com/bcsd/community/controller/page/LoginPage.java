package com.bcsd.community.controller.page;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import jakarta.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginPage {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/login")
    public String showLoginForm(Model model, @RequestParam(value = "error", required = false) String error) {
        if (error != null) {
            model.addAttribute("error", error);
        }
        return "login_layout";
    }

    @PostMapping("/login")
    public String login(@RequestParam("email") String email,
                        @RequestParam("password") String password,
                        HttpSession session, Model model) {
        String loginUrl = "http://localhost:8080/api/member/login";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> request = Map.of("email", email, "password", password);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(loginUrl, HttpMethod.POST, entity, Map.class);
            session.setAttribute("loginUser", response.getBody());
            return "redirect:/member/info";
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                model.addAttribute("error", "유효하지 않은 이메일 또는 비밀번호");
            }
            return "login_layout";
        }
    }

    @GetMapping("/info")
    public String getMemberInfo(HttpSession session, Model model) {
        Map<String, Object> loginUser = (Map<String, Object>) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login?error=로그인되어 있지 않습니다";
        }
        model.addAttribute("username", loginUser.get("username"));
        model.addAttribute("email", loginUser.get("email"));
        return "memberInfo";
    }
}