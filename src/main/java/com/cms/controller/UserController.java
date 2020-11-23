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

    private void checkEmailAndPassword(Map<String, String> user){
        if(!user.containsKey("email") || !user.containsKey("password"))
            throw new RuntimeException();
    }

    @PostMapping("/me")
    public ResponseEntity<Object> getCurrentUser(@AuthenticationPrincipal User user) {
        if(user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("잘못된 토큰입니다.");

        return ResponseEntity.ok(user);
    }

    @PostMapping("/join")
    public ResponseEntity<Object> join(@RequestBody Map<String, String> user) {
        // 회원가입: https://webfirewood.tistory.com/m/115?category=672592
        this.checkEmailAndPassword(user);

        return ResponseEntity.ok(
                userService.addNewUser(user.get("email"), user.get("password"))
        );
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody Map<String, String> user) {
        this.checkEmailAndPassword(user);

        User member = userService.getAuthorisedUser(user.get("email"), user.get("password"));

        return ResponseEntity.ok(
            jwtTokenProvider.createToken(member.getUsername(), member.getRoles())
        );
    }

    @ResponseBody
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    Page<User> getAllUserList(Pageable pageable){

        return userService.getAllUserList(pageable);
    }

    @GetMapping(value = "/{id}/roles")
    public ResponseEntity<Object> getGroupRoles(@AuthenticationPrincipal User user, @PathVariable Long id) {
        return ResponseEntity.ok(
                userService.getGroupRoles(id, user)
        );
    }
}
