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
class CmsApplicationTests {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

//	static Group defaultGroup;
	static Group aGroup;
	static Group bGroup;

	static GroupRoles publicGroupRoles;
//	static GroupRoles aGroupAdminRole;
//	static GroupRoles aGroupAdvertiserRole;

	static User superAdminUser;
	static User aGroupAdminUser;
	static User bUser;
//	static User cUser;

	static Content aContent;

	@BeforeAll
	static void init(){
		aGroup = new Group("A");
		bGroup = new Group("B");

		aGroupAdminUser = new User("aGroupAdminUser");

		bUser = new User("bUser");

		aContent = new Content("A", aGroupAdminUser);

		assertTrue(aGroupAdminUser.addGroupRole(aGroup, GroupRoles.Role.ADMIN));

		assertTrue(bUser.addGroupRole(aGroup, GroupRoles.Role.ADVERTISER));
	}

	@Test
	void addGroupAndRoleTest(){
		assertTrue(aGroupAdminUser.addGroup(aGroup));
		assertTrue(aGroupAdminUser.containsGroup(aGroup));
		assertEquals(aGroupAdminUser.getGroups().size(), 1);
		assertEquals(aGroupAdminUser.getGroupRoles(aGroup).size(), 2);

		assertTrue(bUser.containsGroup(aGroup));

		assertTrue(bUser.addGroup(bGroup));
		assertTrue(bUser.containsGroup(bGroup));

		assertEquals(bUser.getGroups().size(), 2);
		assertEquals(bUser.getGroupRoles(aGroup).size(), 2);

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
	}
}
