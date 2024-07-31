package com.bcsd.community.service;

import com.bcsd.community.controller.dto.request.*;
import com.bcsd.community.controller.dto.response.*;
import com.bcsd.community.entity.Member;

import java.util.List;

public interface MemberService {
    MemberResponseDto findByEmail(String email);
    List<MemberResponseDto> findAll();
    MemberResponseDto register(MemberRegisterRequestDto request);
    Member login(MemberLoginRequestDto request);
    public MemberResponseDto edit(String loginEmail,MemberUpdateRequestDto request);
    public void withDraw(String loginEmail);
}
