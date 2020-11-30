package com.cms.controller;

import com.cms.config.ConfigClass;
import com.cms.config.security.JwtTokenProvider;
import com.cms.model.GroupRoles;
import com.cms.model.User;
import com.cms.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/api/user")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @PostMapping("/me")
    public ResponseEntity<Object> getCurrentUser(@AuthenticationPrincipal User user) {
        if(user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("잘못된 토큰입니다.");

        return ResponseEntity.ok(user);
    }

    @ResponseBody
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    Page<User> getAllUserList(@AuthenticationPrincipal User user, Pageable pageable){
        return userService.getAllUserList(user, pageable);
    }
}
