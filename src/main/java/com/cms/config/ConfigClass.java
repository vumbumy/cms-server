package com.cms.config;

import com.cms.model.Group;
import com.cms.model.GroupRoles;
import com.cms.model.User;
import com.cms.repository.GroupRepository;
import com.cms.repository.GroupRolesRepository;
import com.cms.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Component
public class ConfigClass implements ApplicationListener<ContextRefreshedEvent> {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${custom.default.admin_email}")
    private String SUPER_USER_EMAIL;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    GroupRolesRepository groupRolesRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        logger.info("-------------------- onApplicationEvent --------------------");
        logger.info(org.hibernate.Version.getVersionString());

        Group publicGroup = makePublicGroupIfNotExist();
        User superAdmin = getSuperAdmin(publicGroup);
    }

    private User getSuperAdmin(Group publicGroup){
        Optional<User> optionalUser = userRepository.findUserByEmail(SUPER_USER_EMAIL);
        return optionalUser.orElseGet(() -> makeSuperAdmin(publicGroup));

    }

    private User makeSuperAdmin(Group publicGroup){
        User superAdmin = new User(SUPER_USER_EMAIL, publicGroup, GroupRoles.Role.SUPER_ADMIN);

        superAdmin.setActivate(true);
        superAdmin.setPassword(passwordEncoder.encode(superAdmin.getPassword()));

        return userRepository.save(superAdmin);
    }

    private Group makePublicGroupIfNotExist() {
        Optional<Group> optionalGroup = groupRepository.findById(Group.PUBLIC_ID);
        return optionalGroup.orElseGet(() -> groupRepository.save(
                new Group(Group.PUBLIC_ID, Group.PUBLIC_NAME)
        ));
    }

    public Group getPublicGroup(){
        Optional<Group> optionalGroup = groupRepository.findById(Group.PUBLIC_ID);

        return optionalGroup.orElse(
                makePublicGroupIfNotExist()
        );
    }

    public GroupRoles getPublicGroupRoles(){
        Group publicGroup = getPublicGroup();

        Set<GroupRoles.Role> roles = new HashSet<>(Collections.singletonList(GroupRoles.Role.USER));

        return new GroupRoles(publicGroup, roles);
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}