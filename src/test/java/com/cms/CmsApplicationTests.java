package com.cms;

import com.cms.model.*;
import com.cms.service.ContentService;
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
class CmsApplicationTests {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	UserService userService;

	@Autowired
	ContentService contentService;

	static Group defaultGroup;
	static Group aGroup;
	static Group bGroup;

	static GroupRoles SuperAdminRole;
	static GroupRoles aGroupAdminRole;
	static GroupRoles aGroupAdvertiserRole;

	static User superAdminUser;
	static User aGroupAdminUser;
	static User bUser;
	static User cUser;

	static Content aContent;

	@BeforeAll
	static void init(){
		aGroup = new Group("A");
		bGroup = new Group("B");

//		aGroupAdminRole = new GroupRoles(aGroup, GroupRoles.Role.ADMIN);
//		aGroupAdvertiserRole = new GroupRoles(aGroup, GroupRoles.Role.ADVERTISER);

		aGroupAdminUser = new User("aGroupAdminUser");

		bUser = new User("bUser");
		cUser = new User("cUser");

		aContent = new Content();

		assertTrue(aGroupAdminUser.addGroupRole(aGroup, GroupRoles.Role.ADMIN));

		assertTrue(bUser.addGroupRole(aGroup, GroupRoles.Role.ADVERTISER));
	}

	@Test
	void addGroupAndRoleTest(){
		assertTrue(aGroupAdminUser.addGroup(aGroup));
		assertEquals(aGroupAdminUser.getGroups().size(), 1);

		assertTrue(bUser.addGroup(bGroup));
		assertEquals(bUser.getGroups().size(), 2);
		assertEquals(bUser.getGroupRoles(aGroup).size(), 1);

		assertTrue(aGroupAdminUser.addGroupRole(aGroup, GroupRoles.Role.ADMIN));
		assertEquals(aGroupAdminUser.getGroupRoles(aGroup).size(), 2);

		assertTrue(bUser.addGroupRole(aGroup, GroupRoles.Role.ADVERTISER));
		assertEquals(bUser.getGroupRoles(aGroup).size(), 2);
	}

	@Test
	void permissionTest() {
		Permission aUserWritePermission = new Permission(aGroupAdminUser, null, Permission.AccessRights.WRITE_RIGHT);
		Permission aGroupReadPermission = new Permission(null, aGroup, Permission.AccessRights.READ_RIGHT);

		assertTrue(aContent.addPermission(aUserWritePermission));
		assertTrue(aContent.addPermission(aGroupReadPermission));

		assertTrue(contentService.isWritable(aContent, aGroupAdminUser));
		assertTrue(contentService.isReadable(aContent, bUser));

		assertFalse(contentService.isReadable(aContent, cUser));
	}

	@Test
	void groupAdminTest(){
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
