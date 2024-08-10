package com.bcsd.community.controller.page;

import com.bcsd.community.entity.Member;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Controller
public class InfoPage {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/info")
    public String getMemberInfo(Model model,HttpSession session) {
        String apiUrl = "http://localhost:8080/api/member";
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Cookie", "JSESSIONID=" + session.getId()); //
            HttpEntity<String> entity = new HttpEntity<>(headers);

            // RestTemplate의 exchange 메서드를 사용하여 GET 요청 전송
            ResponseEntity<MemberResponse> responseEntity = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.GET,
                    entity,
                    MemberResponse.class
            );
            HttpStatusCode statusCode = responseEntity.getStatusCode();

            // 상태 코드 확인 및 처리
            if (statusCode == HttpStatus.OK) {
                MemberResponse response = responseEntity.getBody();

                // 모델에 속성 추가
                model.addAttribute("username", response.getUsername());
                model.addAttribute("email", response.getEmail());
                model.addAttribute("joinDate", response.getCreatedAt());

                return "info_layout";
            }
        }catch (RuntimeException e) {
            return "redirect:/login";
        }
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

    public static class MemberResponse {
        private long id;
        private String username;
        private String email;
        private String createdAt;

        // Getters and Setters
        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }
    }
}
