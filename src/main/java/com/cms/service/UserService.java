package com.cms.service;

import com.cms.model.*;
import com.cms.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@Service
public class UserService {
    public Boolean isGroupAdmin(User admin, Group group){
        HashMap<Group, Collection<GroupRoles.Role>> groupListMap = admin.getGroupListMap();
        if(groupListMap == null)
            return false;

        if(!groupListMap.containsKey(group))
            return false;

        Collection<GroupRoles.Role> roles = groupListMap.get(group);
        if(roles == null)
            return false;

        return roles.contains(GroupRoles.Role.ADMIN);
    }
}
