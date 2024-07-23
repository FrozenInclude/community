package com.bcsd.community.service;

import com.bcsd.community.controller.dto.request.MemberLoginRequestDto;
import com.bcsd.community.controller.dto.request.MemberRegisterRequestDto;
import com.bcsd.community.controller.dto.request.MemberUpdateRequestDto;
import com.bcsd.community.controller.dto.response.MemberResponseDto;
import com.bcsd.community.entity.Member;
import com.bcsd.community.repository.MemberRepository;
import com.bcsd.community.util.PasswordUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MemberServiceImp implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional(readOnly = true)
    public MemberResponseDto findByEmail(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(IllegalArgumentException::new);
        return MemberResponseDto.from(member);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MemberResponseDto> findAll() {
        List<Member> members = memberRepository.findAll();
        return members.stream()
                .map(MemberResponseDto::from)
                .toList();
    }

    @Override
    @Transactional
    public MemberResponseDto register(MemberRegisterRequestDto request) {
        Member member = memberRepository.save(request.toEntity());
        return MemberResponseDto.from(member);
    }

    @Override
    @Transactional(readOnly = true)
    public Member login(MemberLoginRequestDto request) {
        return memberRepository.findByEmail(request.email())
                .filter(user -> PasswordUtils.verifyPassword(request.password(), user.getPassword(), user.getSalt()))
                .orElse(null);
    }

    @Override
    @Transactional
    public void withDraw(String loginEmail) {
        Member member=memberRepository.findByEmail(loginEmail)
                .orElseThrow(IllegalArgumentException::new);;
        memberRepository.delete(member);
    }

    @Override
    @Transactional
    public MemberResponseDto edit(String loginEmail, MemberUpdateRequestDto request) {
        Member member = memberRepository.findByEmail(loginEmail).orElseThrow(IllegalArgumentException::new);
        member.update(request);
        return MemberResponseDto.from(member);
    }
}