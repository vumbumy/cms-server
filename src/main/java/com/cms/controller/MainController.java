package com.cms.controller;

import com.cms.config.dto.UserDTO;
import com.cms.config.security.JwtTokenProvider;
import com.cms.model.User;
import com.cms.service.UserService;
import com.cms.service.VerificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(value = "/api")
public class MainController {
    @Autowired
    UserService userService;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    VerificationService verificationService;


    @PostMapping("/me")
    public ResponseEntity<Object> getCurrentUser(@AuthenticationPrincipal User user) {
        if(user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("잘못된 토큰입니다.");

        return ResponseEntity.ok(user);
    }

    @PostMapping("/join")
    public ResponseEntity<Object> join(@RequestBody @Valid UserDTO userDTO) {
        // 회원가입: https://webfirewood.tistory.com/m/115?category=672592
        User user = userService.addNewUser(userDTO);

        return ResponseEntity.ok(
                verificationService.sendVerificationCodeEmail(user)
        );
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody @Valid UserDTO userDTO) {
        User member = userService.getAuthorisedUser(userDTO);

        return ResponseEntity.ok(
                jwtTokenProvider.createToken(member.getUsername(), member.getRoles())
        );
    }

    @GetMapping("/verify/{code}")
    public ResponseEntity<Object> verify(@PathVariable String code) {
        User user = null;

        user = verificationService.verifyCode(code);
        user = userService.userActivate(user);

        return ResponseEntity.ok(
            jwtTokenProvider.createToken(user.getUsername(), user.getRoles())
        );
    }
}
