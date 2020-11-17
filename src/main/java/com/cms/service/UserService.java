package com.cms.service;

import com.cms.config.ConfigClass;
import com.cms.config.security.JwtTokenProvider;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ConfigClass configClass;

    public Long addNewUser(String email, String password){
        Optional<User> userOptional = userRepository.findUserByEmail(email);
        if(userOptional.isPresent())
            return 0L;

        Group publicGroup = configClass.getPublicGroup();

        User user = new User(email);
        user.addGroupRole(publicGroup, GroupRoles.Role.USER);
        user.setPassword(passwordEncoder.encode(password));

        return userRepository.save(user).getId();
    }

    public User getAuthorisedUser(String email, String password){
        User member = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        return member;
    }

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
