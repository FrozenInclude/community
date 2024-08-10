package com.bcsd.community.controller.page;

import com.bcsd.community.entity.Member;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Controller
@AllArgsConstructor
public class UpdateProfilePage {

    private RestTemplate restTemplate;

    @GetMapping("/updateProfile")
    public String showUpdateProfileForm(HttpSession session,Model model) {
        if(session.getAttribute("loginUser")==null){
            return "redirect:/login";
        }
        return "updateProfile_layout";
    }

    @PostMapping("/updateProfile")
    public String updateProfile(@RequestParam("username") String username,
                                @RequestParam("password") String password,
                                @RequestParam("confirmPassword") String confirmPassword,
                                HttpSession session,
                                Model model) {

        String updateUrl = "http://localhost:8080/api/member";
        Map<String, String> request = new HashMap<>();
        if (!Objects.equals(username, "")) request.put("username", username);
        if (!Objects.equals(password, "")) request.put("password", password);
        request.put("confirm_password", confirmPassword);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Cookie", "JSESSIONID=" + session.getId()); // 세션 쿠키 추가
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);

        try {
            restTemplate.exchange(updateUrl, HttpMethod.PUT, entity, Void.class);
            return "redirect:/info";
        } catch (HttpClientErrorException e) {
            String errorMessage = e.getResponseBodyAsString();
            Map<String, String> errorMap = new HashMap<>();
            ObjectMapper objectMapper = new ObjectMapper();

            try {
                errorMap = objectMapper.readValue(errorMessage, new TypeReference<Map<String, String>>() {});
            } catch (JsonProcessingException jsonException) {
                errorMap.put("general", "처리 중 오류가 발생했습니다.");
            }

            model.addAttribute("error", errorMap);
            return "updateProfile_layout";
        }
    }
}