package com.cms.controller;

import com.cms.config.ConfigClass;
import com.cms.config.security.JwtTokenProvider;
import com.cms.model.GroupRoles;
import com.cms.model.User;
import com.cms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/api/user")
public class UserController {
    @Autowired
    ConfigClass configClass;

    @Autowired
    UserService userService;

    @Autowired
    JwtTokenProvider jwtTokenProvider;



    // 회원가입: https://webfirewood.tistory.com/m/115?category=672592
    @PostMapping("/join")
    public Long join(@RequestBody Map<String, String> user) {
        GroupRoles publicGroupRoles = configClass.getPublicGroupRoles();
//        return userRepository.save(User.builder()
//                .email(user.get("email"))
//                .password(passwordEncoder.encode(user.get("password")))
//                .roles(Collections.singletonList("ROLE_USER")) // 최초 가입시 USER 로 설정
//                .build()).getId();
        return 0L;
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody Map<String, String> user) {
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
}
