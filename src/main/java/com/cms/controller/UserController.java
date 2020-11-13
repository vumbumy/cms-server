package com.cms.controller;

import com.cms.model.User;
import com.cms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/user")
public class UserController {
    @Autowired
    UserService userService;

    @ResponseBody
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    Page<User> getAllUserList(Pageable pageable){
        return userService.getAllUserList(pageable);
    }
}
