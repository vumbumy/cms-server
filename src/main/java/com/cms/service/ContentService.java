package com.cms.service;

import com.cms.model.Content;
import com.cms.model.Group;
import com.cms.model.Permission;
import com.cms.model.User;
import com.cms.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Service
public class ContentService {

    @Autowired
    PermissionRepository permissionRepository;

    public Boolean isReadable(Content content, User user){
        if(content.isAuthor(user))
            return true;

        Collection<Permission> permissionList = content.getPermissions();

        Set<Group> userGroups = user.getGroups();
        for(Permission permission : permissionList){
            if(user.equals(permission.getUser()))
                return permission.isReadable();

            if(userGroups != null && userGroups.contains(permission.getGroup()))
                return permission.isReadable();
        }

        return false;
    }

    public Boolean isWritable(Content content, User user){
        if(content.isAuthor(user))
            return true;

        Collection<Permission> permissionList = content.getPermissions();

        Set<Group> userGroups = user.getGroups();
        for(Permission permission : permissionList){
            if(user.equals(permission.getUser()))
                return permission.isWriteable();

            if(userGroups != null && userGroups.contains(permission.getGroup()))
                return permission.isWriteable();
        }

        return false;
    }
}
