package com.bcsd.community.service;

import com.bcsd.community.controller.dto.request.MemberLoginRequestDto;
import com.bcsd.community.controller.dto.request.MemberRegisterRequestDto;
import com.bcsd.community.controller.dto.request.MemberUpdateRequestDto;
import com.bcsd.community.controller.dto.response.ArticleResponseDto;
import com.bcsd.community.controller.dto.response.BoardResponseDto;
import com.bcsd.community.controller.dto.response.CommentResponseDto;
import com.bcsd.community.controller.dto.response.MemberResponseDto;
import com.bcsd.community.entity.Member;
import com.bcsd.community.repository.MemberRepository;
import com.bcsd.community.service.exception.AlreadyExistsException;
import com.bcsd.community.service.exception.UseremailNotFoundException;
import com.bcsd.community.util.PasswordUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
        memberRepository.findByEmail(request.email())
                .ifPresent(email -> {
                    throw new AlreadyExistsException("email");
                });
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
    @Transactional(readOnly = true)
    public List<ArticleResponseDto> getArticles(String loginEmail) {
        return memberRepository.findByEmail(loginEmail)
                .map(Member::getArticles)
                .orElseThrow(() -> new UseremailNotFoundException("유저 "+loginEmail+"을 찾을수 없습니다."))
                .stream()
                .map(ArticleResponseDto::from)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BoardResponseDto> getBoards(String loginEmail) {
        return memberRepository.findByEmail(loginEmail)
                .map(Member::getBoards)
                .orElseThrow(() -> new UseremailNotFoundException("유저 "+loginEmail+"을 찾을수 없습니다."))
                .stream()
                .map(BoardResponseDto::from)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentResponseDto> getComments(String loginEmail) {
        return memberRepository.findByEmail(loginEmail)
                .map(Member::getComments)
                .orElseThrow(() -> new UseremailNotFoundException("유저 "+loginEmail+"을 찾을수 없습니다."))
                .stream()
                .map(CommentResponseDto::from)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void withDraw(String loginEmail) {
        Member member = memberRepository.findByEmail(loginEmail)
                .orElseThrow(IllegalArgumentException::new);

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