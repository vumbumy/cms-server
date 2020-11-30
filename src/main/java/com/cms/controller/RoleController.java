package com.cms.controller;

import com.cms.config.security.JwtTokenProvider;
import com.cms.model.User;
import com.cms.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/api/roles")
public class RoleController {
    @Autowired
    UserService userService;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @GetMapping(value = "/group/{id}")
    public ResponseEntity<Object> getGroupRoles(@AuthenticationPrincipal User user, @PathVariable Long id) {
        return ResponseEntity.ok(
                userService.getGroupRoles(id, user)
        );
    }
}
