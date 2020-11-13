package com.cms.service;

import com.cms.model.Group;
import com.cms.model.GroupRoles;
import com.cms.model.User;
import com.cms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    public Page<User> getAllUserList(Pageable pageable){
        return userRepository.findAll(pageable);
    }

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

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findUserByEmail(email);
        if(!userOptional.isPresent()) {
            throw new UsernameNotFoundException(email);
        }

        return userOptional.get();
    }
}
