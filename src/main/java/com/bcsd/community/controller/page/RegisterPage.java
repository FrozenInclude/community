package com.bcsd.community.controller.page;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Controller
public class RegisterPage {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/register")
    public String showRegisterForm(Model model, @RequestParam(value = "error", required = false) String error) {
        if (error != null) {
            model.addAttribute("error", error);
        }
        return "register_layout";
    }

    @PostMapping("/register")
    public String register(@RequestParam("username") String username,
                           @RequestParam("email") String email,
                           @RequestParam("password") String password,
                           @RequestParam("confirmPassword") String confirmPassword,
                           Model model) {
        if (!password.equals(confirmPassword)) {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("confirmPassword", "비밀번호가 일치하지 않습니다.");
            model.addAttribute("error", errorMap);
            return "register_layout";
        }

        String registerUrl = "http://localhost:8080/api/member";
        Map<String, String> request = new HashMap<>();
        request.put("username", username);
        request.put("email", email);
        request.put("password", password);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(registerUrl, HttpMethod.POST, entity, Map.class);
            return "redirect:/login";
        } catch (HttpClientErrorException e) {
            String errorMessage = e.getResponseBodyAsString();
            Map<String, String> errorMap = new HashMap<>();
            ObjectMapper objectMapper = new ObjectMapper();

            try {
                errorMap = objectMapper.readValue(errorMessage, new TypeReference<Map<String, String>>() {
                });
            } catch (JsonProcessingException jsonException) {
                errorMap.put("general", "처리 중 오류가 발생했습니다.");
            }

            model.addAttribute("error", errorMap);
            return "register_layout";
        }
    }
}
