package com.cms.service;

import com.cms.model.Group;
import com.cms.model.GroupRoles;
import com.cms.model.User;
import com.cms.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GroupService {
//    @Autowired
//    GroupRepository groupRepository;
//
//    public Group getPublicGroup(){
//        Optional<Group> optionalGroup = groupRepository.findById(Group.PUBLIC_ID);
//
//        return optionalGroup.orElse(null);
//    }
//
//    public GroupRoles getPublicGroupRoles(){
//        Group publicGroup = getPublicGroup();
//
//        Set<GroupRoles.Role> roles = new HashSet<>(Collections.singletonList(GroupRoles.Role.USER));
//
//        return new GroupRoles(publicGroup, roles);
//    }
}
