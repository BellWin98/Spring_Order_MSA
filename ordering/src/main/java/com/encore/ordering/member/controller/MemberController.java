package com.encore.ordering.member.controller;

import com.encore.ordering.common.CommonResponse;
import com.encore.ordering.member.domain.Member;
import com.encore.ordering.member.dto.request.CreateMemberRequest;
import com.encore.ordering.member.dto.request.LoginRequest;
import com.encore.ordering.member.dto.response.MemberResponse;
import com.encore.ordering.member.service.MemberService;
import com.encore.ordering.order.service.OrderService;
import com.encore.ordering.securities.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MemberController {
    private final MemberService memberService;
    private final OrderService orderService;
    private final JwtProvider jwtProvider;

    @Autowired
    public MemberController(MemberService memberService, OrderService orderService, JwtProvider jwtProvider){
        this.memberService = memberService;
        this.orderService = orderService;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/member/create")
    public ResponseEntity<CommonResponse> create(@Valid @RequestBody CreateMemberRequest createMemberRequest){
        Member member = memberService.create(createMemberRequest);
        return new ResponseEntity<>(new CommonResponse(HttpStatus.CREATED,
                "Member Successfully Created", member.getId()), HttpStatus.CREATED);
    }

    // 특정 회원의 주문 내역 조회 (관리자 권한)
    // 용도: 회원 목록 조회 -> 회원별 주문 목록 조회
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/member/{id}/orders")
    public ResponseEntity<CommonResponse> findMemberOrders(@PathVariable Long id){
        return new ResponseEntity<>(new CommonResponse(HttpStatus.OK,
                "Member Orders Successfully Found", orderService.findMemberOrders(id)), HttpStatus.OK);
    }

    // 내 주문 내역 조회
    // 토큰 안에 있는 이메일 정보와, 매개변수를 통해 넘어온 이메일이 동일할 때
//    @PreAuthorize("hasRole('ADMIN') or #email == authentication.principal.username")
    @GetMapping("/member/my-orders")
    public ResponseEntity<CommonResponse> findMyOrders(){
        return new ResponseEntity<>(new CommonResponse(HttpStatus.OK,
                "My Orders Successfully Found", orderService.findMyOrders()), HttpStatus.OK);
    }

    @PostMapping("/doLogin")
    public ResponseEntity<CommonResponse> login(@Valid @RequestBody LoginRequest loginRequest){
        Member member = memberService.login(loginRequest);
        // 토큰 생성
        String jwt = jwtProvider.createToken(member.getEmail(), member.getRole().toString());
        Map<String, Object> member_info = new HashMap<>();
        member_info.put("id", member.getId());
        member_info.put("token", jwt);

        return new ResponseEntity<>(new CommonResponse(HttpStatus.OK,
                "Member Successfully Login", member_info), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')") // 관리자 권한만 조회 가능
    @GetMapping("/members")
    public ResponseEntity<CommonResponse> findMembers(){
        List<MemberResponse> memberResponses = memberService.findMembers();
        return new ResponseEntity<>(new CommonResponse(HttpStatus.OK,
                "Get Member List", memberResponses), HttpStatus.OK);
    }

    @GetMapping("/member/myInfo")
    public ResponseEntity<CommonResponse> findMyInfo(){
        MemberResponse memberResponse = memberService.findMyInfo();
        return new ResponseEntity<>(new CommonResponse(HttpStatus.OK,
                "Member Successfuly Found", memberResponse), HttpStatus.OK);
    }
}
