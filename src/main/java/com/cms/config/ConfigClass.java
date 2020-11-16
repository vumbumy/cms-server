package com.cms.config;

import com.cms.model.Group;
import com.cms.model.GroupRoles;
import com.cms.model.User;
import com.cms.repository.GroupRepository;
import com.cms.repository.GroupRolesRepository;
import com.cms.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class ConfigClass implements ApplicationListener<ContextRefreshedEvent> {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    GroupRolesRepository groupRolesRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        logger.info("-------------------- onApplicationEvent --------------------");
        logger.info(org.hibernate.Version.getVersionString());

        Group publicGroup = makePublicGroupIfNotExist();
        User superAdmin = makeSuperAdmin(publicGroup);
    }

    private User makeSuperAdmin(Group publicGroup){
        User superAdmin = new User("admin@example.com", publicGroup, GroupRoles.Role.SUPER_ADMIN);

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
}