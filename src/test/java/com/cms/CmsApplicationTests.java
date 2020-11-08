package com.cms;

import com.cms.model.*;
import com.cms.service.ContentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.*;

@SpringBootTest
class CmsApplicationTests {
	@Autowired
	ContentService contentService;

	@Test
	void permissionTest() {
		Group aGroup = new Group();

		User aUser = new User();
		GroupRoles aGroupAdmin = new GroupRoles(aGroup, GroupRoles.Role.ADMIN);
		assertTrue(aUser.addGroupRoles(aGroupAdmin));

		User bUser = new User();
		GroupRoles aGroupAdvertiser = new GroupRoles(aGroup, GroupRoles.Role.ADVERTISER);
		assertTrue(bUser.addGroupRoles(aGroupAdvertiser));

		Content aContent = new Content();

		Permission aUserWritePermission = new Permission(aUser, null, Permission.AccessRights.WRITE_RIGHT);
		Permission aGroupReadPermission = new Permission(null, aGroup, Permission.AccessRights.READ_RIGHT);

		assertTrue(aContent.addPermission(aUserWritePermission));
		assertTrue(aContent.addPermission(aGroupReadPermission));

		assertTrue(contentService.isWritable(aContent, aUser));
		assertTrue(contentService.isReadable(aContent, bUser));
	}

}
