package com.cms;

import com.cms.config.ConfigClass;
import com.cms.model.*;
import com.cms.service.ContentService;
import com.cms.service.GroupService;
import com.cms.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
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

	@Autowired
	ConfigClass configClass;

	@Test
	void permissionTest() {
		// TODO: Get/Set Test와 Service Test를 분명하게 분리해서 관리할 필요가 있음.

		Group publicGroup = configClass.getPublicGroup();
		assertNotNull(publicGroup);
		assertEquals(publicGroup.getName(), Group.PUBLIC_NAME);

		Group aGroup = new Group("A");

//		GroupRoles publicGroupRoles = groupService.getPublicGroupRoles();

		User aGroupAdminUser = new User("aGroupAdminUser", publicGroup, GroupRoles.Role.SUPER_ADMIN);
		assertEquals(aGroupAdminUser.getGroupRolesList().size(), 1);

		User bUser = new User("bUser", publicGroup);
		assertTrue(bUser.addGroup(aGroup));
		assertNotNull(bUser.getGroupRolesList());
		assertEquals(bUser.getGroupRolesList().size(), 2);

		User cUser = new User("cUser", publicGroup);
		assertEquals(cUser.getGroupRolesList().size(), 1);

//		Permission publicPermission = new Permission(null, publicGroup, Permission.AccessRights.READ_RIGHT);
//		Permission bUserWritePermission = new Permission(bUser, null, Permission.AccessRights.WRITE_RIGHT);
//		Permission aGroupReadPermission = new Permission(null, aGroup, Permission.AccessRights.READ_RIGHT);

//		Content aContent = new Content("A", aGroupAdminUser);
//		assertTrue(aContent.addPermission(aGroupReadPermission));

//		Content bContent = new Content("B", aGroupAdminUser);
//		assertTrue(bContent.addPermission(bUserWritePermission));

//		Content cContent = new Content("C", aGroupAdminUser);
//		assertTrue(cContent.addPermission(publicPermission));

//		Content dContent = new Content("D", bUser);
//		assertTrue(dContent.addPermission(publicPermission));

//		assertTrue(contentService.isWritable(aContent, aGroupAdminUser));
//		assertTrue(contentService.isReadable(bContent, aGroupAdminUser));
//		assertTrue(contentService.isWritable(dContent, aGroupAdminUser));

//		assertFalse(contentService.isWritable(aContent, bUser));
//		assertTrue(contentService.isReadable(aContent, bUser));
//		assertTrue(contentService.isWritable(bContent, bUser));

//		assertFalse(contentService.isReadable(aContent, cUser));
//		assertFalse(contentService.isReadable(bContent, cUser));
//		assertTrue(contentService.isReadable(cContent, cUser));
	}

	@Test
	void groupAdminTest(){
		Group aGroup = new Group("A");
		Group bGroup = new Group("B");

		User aGroupAdminUser = new User("aGroupAdminUser");
		aGroupAdminUser.addGroupRole(aGroup, GroupRoles.Role.ADMIN);

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
