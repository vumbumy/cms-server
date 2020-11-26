package com.cms.controller;

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
@RequestMapping(value = "/api/user")
public class UserController {
    @Autowired
    UserService userService;

    @ResponseBody
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    Page<User> getAllUserList(@AuthenticationPrincipal User user, Pageable pageable){
        return userService.getAllUserList(user, pageable);
    }

    @GetMapping(value = "/{id}/roles")
    public ResponseEntity<Object> getGroupRoles(@AuthenticationPrincipal User user, @PathVariable Long id) {
        return ResponseEntity.ok(
                userService.getGroupRoles(id, user)
        );
    }
}
