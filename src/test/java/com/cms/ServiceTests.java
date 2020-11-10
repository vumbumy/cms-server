package com.cms;

import com.cms.model.*;
import com.cms.service.ContentService;
import com.cms.service.GroupService;
import com.cms.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collection;
import java.util.HashMap;

import static org.junit.Assert.*;

@SpringBootTest
class ServiceTests {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	UserService userService;

	@Autowired
	GroupService groupService;

	@Autowired
	ContentService contentService;

	@Test
	void permissionTest() {
		Group publicGroup = groupService.getPublicGroup();
		assertNotNull(publicGroup);
		assertEquals(publicGroup.getName(), Group.PUBLIC_NAME);

		Group aGroup = new Group("A");

		GroupRoles publicGroupRoles = groupService.getPublicGroupRoles();

		User aGroupAdminUser = new User("aGroupAdminUser", publicGroupRoles);
		User bUser = new User("bUser", publicGroupRoles);
		User cUser = new User("cUser", publicGroupRoles);

		Permission publicPermission = new Permission(null, publicGroup, Permission.AccessRights.READ_RIGHT);
		Permission aUserWritePermission = new Permission(aGroupAdminUser, null, Permission.AccessRights.WRITE_RIGHT);
		Permission aGroupReadPermission = new Permission(null, aGroup, Permission.AccessRights.READ_RIGHT);

		Content aContent = new Content();

		assertTrue(aContent.addPermission(aUserWritePermission));
		assertTrue(aContent.addPermission(aGroupReadPermission));

		Content bContent = new Content();
		assertTrue(bContent.addPermission(publicPermission));

		assertNotNull(bUser.getGroupRolesList());
		assertEquals(bUser.getGroupRolesList().size(), 1);

//		for(GroupRoles groupRoles : bUser.getGroupRolesList()){
//			logger.info(groupRoles.getGroup().getName());
//		}

//		HashMap<Group, Collection<GroupRoles.Role>> bUserGroupListMap = bUser.getGroupListMap();
//		assertEquals(bUserGroupListMap.size(), 1);

//		assertTrue(contentService.isWritable(aContent, aGroupAdminUser));
//		assertTrue(contentService.isReadable(aContent, bUser));

//		assertFalse(contentService.isReadable(aContent, cUser));
//		assertTrue(contentService.isReadable(bContent, cUser));
	}

	@Test
	void groupAdminTest(){
		Group aGroup = new Group("A");
		Group bGroup = new Group("B");

		User aGroupAdminUser = new User("aGroupAdminUser");

		assertTrue(userService.isGroupAdmin(aGroupAdminUser, aGroup));

		HashMap<Group, Collection<GroupRoles.Role>> aGroupAdminRolesMap = aGroupAdminUser.getGroupListMap();
//		for(Group key : aGroupAdminRolesMap.keySet()){
//			logger.info(key.getName());
//		}

		assertFalse(aGroupAdminRolesMap.containsKey(bGroup));
		assertNull(aGroupAdminRolesMap.get(bGroup));

		assertFalse(userService.isGroupAdmin(aGroupAdminUser, bGroup));
	}
}
