package com.cms.service;

import com.cms.model.Group;
import com.cms.model.GroupRoles;
import com.cms.model.User;
import com.cms.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;

@Service
public class GroupService {
    @Autowired
    GroupRepository groupRepository;

    public Group getPublicGroup(){
        Optional<Group> optionalGroup = groupRepository.findById(Group.PUBLIC_ID);

        return optionalGroup.orElse(null);
    }

    public GroupRoles getPublicGroupRoles(){
        Group publicGroup = getPublicGroup();

        return new GroupRoles(publicGroup, Collections.singletonList(GroupRoles.Role.USER));
    }
}
