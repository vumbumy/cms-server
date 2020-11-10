package com.cms.config;

import com.cms.model.Group;
import com.cms.model.GroupRoles;
import com.cms.repository.GroupRepository;
import com.cms.repository.GroupRolesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class InitialSetup implements ApplicationListener<ContextRefreshedEvent> {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    GroupRolesRepository groupRolesRepository;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        logger.info("-------------------- onApplicationEvent --------------------");
        logger.info(org.hibernate.Version.getVersionString());

        Group publicGroup = makePublicGroupIfNotExist();
    }

    private Group makePublicGroupIfNotExist() {
//        Group publicGroup = null;

//        Optional<Group> optionalGroup = groupRepository.findById(Group.PUBLIC_ID);
//        publicGroup = optionalGroup.orElseGet(() -> groupRepository.save(
//                new Group(Group.PUBLIC_ID, Group.PUBLIC_NAME)
//        ));

        Group publicGroup = groupRepository.getOne(Group.PUBLIC_ID);
        if(publicGroup == null) {
            publicGroup = new Group(Group.PUBLIC_ID, Group.PUBLIC_NAME);
        }
//        publicGroup = optionalGroup.orElseGet(() -> groupRepository.save(
//                new Group(Group.PUBLIC_ID, Group.PUBLIC_NAME)
//        ));

        return publicGroup;
    }
}