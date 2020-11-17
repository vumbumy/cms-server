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
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@ControllerAdvice
@Slf4j
@RestController
@RequestMapping(value = "/api/user")
public class UserController {
    // 400
    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<Object> BadRequestException(final RuntimeException ex) {
        log.warn("error", ex);
        return ResponseEntity.badRequest().body(ex.getMessage());
    }


    // 401
    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<Object> handleIllegalArgumentException(final IllegalArgumentException ex) {
        log.warn("error", ex);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }


    // 500
    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleAll(final Exception ex) {
        log.info(ex.getClass().getName());
        log.error("error", ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Autowired
    UserService userService;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    private void checkEmailAndPassword(Map<String, String> user){
        if(!user.containsKey("email") || !user.containsKey("password"))
            throw new RuntimeException();
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
}
