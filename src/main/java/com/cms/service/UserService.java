package com.cms.service;

import com.cms.config.ConfigClass;
import com.cms.config.Exception.UserNotFoundException;
import com.cms.dto.UserDto;
import com.cms.model.Group;
import com.cms.model.GroupRoles;
import com.cms.model.User;
import com.cms.repository.GroupRepository;
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
import java.util.Set;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ConfigClass configClass;

    public User addNewUser(UserDto userDTO){
        Optional<User> userOptional = userRepository.findUserByEmail(userDTO.email);
        if(userOptional.isPresent())
            throw  new IllegalArgumentException("이미 가입된 E-MAIL 주소입니다.");

        Group publicGroup = configClass.getPublicGroup();

        User user = new User(userDTO.email);
        user.addGroupRole(publicGroup, GroupRoles.Role.USER);
        user.setPassword(passwordEncoder.encode(userDTO.password));

        return userRepository.save(user);
    }

    public User getAuthorisedUser(UserDto userDTO){
        User member = userRepository.findUserByEmail(userDTO.email)
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));
        if (!passwordEncoder.matches(userDTO.password, member.getPassword()))
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");

        if(!member.isEnabled())
            throw new IllegalArgumentException("인증되지 않은 E-MAIL 입니다.");

        return member;
    }

    public User getUserByEmail(String email){
        Optional<User> userOptional = userRepository.findUserByEmail(email);
        return userOptional.orElseThrow(UserNotFoundException::new);
    }

    public Page<User> getAllUserList(User user, Pageable pageable){
        if(user == null) throw new UserNotFoundException();

        if(user.isSuperAdmin())
            return userRepository.findAll(pageable);

        return null;
    }

    public Page<User> getGroupUserList(Long groupId, User user, Pageable pageable){
        if(user == null) throw new UserNotFoundException();

        Group group = getGroupInfo(groupId, user);
        if(group == null) return null;

//        if(!user.isAdmin(group)) return null;

        return userRepository.findAllByGroupsContains(group, pageable);
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

    public Group getGroupInfo(Long groupId, User user){
        if(user == null) throw new UserNotFoundException();

        Optional<Group> optionalGroup = groupRepository.findById(groupId);
        if(!optionalGroup.isPresent()){
            return null;
        }

        Group group = optionalGroup.get();
        if(user.containsGroup(group))
            return group;

        return null;
    }

    public User userActivate(User user){
        if(user == null) throw new UserNotFoundException();

        user.setActivate(true);

        return userRepository.save(user);
    }

    public Set<GroupRoles.Role> getGroupRoles(Long groupId, User user){
        if(user == null) throw new UserNotFoundException();

        Optional<Group> optionalGroup = groupRepository.findById(groupId);
        if(!optionalGroup.isPresent()){
            return null;
        }

        Group group = optionalGroup.get();
        if(user.containsGroup(group))
            return user.getGroupRoles(group);

        return null;
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
