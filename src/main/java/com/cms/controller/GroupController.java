package com.cms.controller;

import com.cms.model.User;
import com.cms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/group")
public class GroupController {
    @Autowired
    UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<Object> getGroupInfo(@AuthenticationPrincipal User user, @PathVariable Long id) {
        return ResponseEntity.ok(
                userService.getGroupInfo(id, user)
        );
    }
}
