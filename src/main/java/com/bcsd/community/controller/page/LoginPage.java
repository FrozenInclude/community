package com.bcsd.community.controller.page;


import com.bcsd.community.entity.Member;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
public class LoginPage {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/login")
    public String showLoginForm(HttpSession session,Model model, @RequestParam(value = "error", required = false) String error) {
        if (session.getAttribute("loginUser") != null) return "redirect:/info";
        if (error != null) {
            model.addAttribute("error", error);
        }
        return "login_layout";
    }

    @PostMapping("/login")
    public String login(@RequestParam("email") String email,
                        @RequestParam("password") String password,
                        HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Model model) {
        String loginUrl = "http://localhost:8080/api/member/login";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> request = Map.of("email", email, "password", password);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(loginUrl, HttpMethod.POST, entity, Map.class);

            String jsessionId = getString(response);

            HttpSession session = httpServletRequest.getSession();
            Cookie sessionCookie = new Cookie("JSESSIONID", jsessionId);
            sessionCookie.setPath("/");
            sessionCookie.setHttpOnly(true);
            httpServletResponse.addCookie(sessionCookie);
            return "redirect:/info";
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                model.addAttribute("error", "유효하지 않은 이메일 또는 비밀번호");
            }
            return "login_layout";
        }
    }

    private static String getString(ResponseEntity<Map> response) {
        HttpHeaders header = response.getHeaders();

        List<String> cookies = header.get(HttpHeaders.SET_COOKIE);
        String jsessionId = null;

        if (cookies != null) {
            for (String cookie : cookies) {
                if (cookie.startsWith("JSESSIONID")) {
                    // JSESSIONID=... 형태에서 실제 세션 ID만 추출
                    jsessionId = cookie.split(";")[0].split("=")[1];
                    break;
                }
            }
        }
        return jsessionId;
    }
}