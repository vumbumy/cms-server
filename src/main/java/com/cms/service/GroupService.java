package com.cms.service;

import com.cms.config.Exception.UserNotFoundException;
import com.cms.model.Group;
import com.cms.model.GroupRoles;
import com.cms.model.User;
import com.cms.repository.GroupRepository;
import com.cms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GroupService {
    @Autowired
    GroupRepository groupRepository;

    @Autowired
    UserRepository userRepository;

//    public Group getPublicGroup(){
//        Optional<Group> optionalGroup = groupRepository.findById(Group.PUBLIC_ID);
//
//        return optionalGroup.orElse(null);
//    }

//    public GroupRoles getPublicGroupRoles(){
//        Group publicGroup = getPublicGroup();
//
//        Set<GroupRoles.Role> roles = new HashSet<>(Collections.singletonList(GroupRoles.Role.USER));
//
//        return new GroupRoles(publicGroup, roles);
//    }

    public Group addNewGroup(Group group, User user){
        Group newGroup = groupRepository.save(group);

        if(!user.addGroupRole(newGroup, GroupRoles.Role.ADMIN))
            throw new RuntimeException("Fail to add user on new group");
        else
            userRepository.save(user);

        return newGroup;
    }

    public Collection<Group> getGroupList(User user){
        if(user == null) throw new UserNotFoundException();

        if(user.isSuperAdmin())
            return groupRepository.findAll();
        else
            return user.getGroups();
    }
}
