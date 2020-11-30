package com.cms.controller;

import com.cms.model.Group;
import com.cms.model.GroupRoles;
import com.cms.model.User;
import com.cms.service.GroupService;
import com.cms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping(value = "/api")
public class GroupController {
    @Autowired
    UserService userService;

    @Autowired
    GroupService groupService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/group/list")
    public Collection<Group> getGroupList(@AuthenticationPrincipal User user) {
        return groupService.getGroupList(user);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/group/{id}")
    public Group getGroupInfo(@AuthenticationPrincipal User user, @PathVariable Long id) {
        return userService.getGroupInfo(id, user);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/group/{id}/roles")
    public Collection<GroupRoles.Role> getGroupRoles(@AuthenticationPrincipal User user, @PathVariable Long id) {
        return userService.getGroupRoles(id, user);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/group")
    public Group createNewGroup(@AuthenticationPrincipal User user, @RequestBody Group newGroup) {
        return groupService.addNewGroup(newGroup, user);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/group/{id}/users", method = RequestMethod.GET)
    Page<User> getAllUserList(@AuthenticationPrincipal User user, @PathVariable Long id, Pageable pageable){
        return userService.getGroupUserList(id, user, pageable);
    }
}
