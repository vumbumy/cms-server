package com.cms.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    static Group aGroup;
    static Group bGroup;

//    static User aGroupAdminUser;
//    static User bUser;

//    static Content aContent;

    @BeforeAll
    static void init(){
        aGroup = new Group("A");
        bGroup = new Group("B");

//        aGroupAdminUser = new User("aGroupAdminUser");

//        bUser = new User("bUser");

//        aContent = new Content("A", aGroupAdminUser);

//        assertTrue(bUser.addGroupRole(aGroup, GroupRoles.Role.ADVERTISER));
    }

    @Test
    void getRoles() {
    }

    @Test
    void getAuthorities() {
        User aUser = new User("aUser@example.com");
        aUser.addGroup(aGroup);

        assertEquals(aUser.getAuthorities().size(), 1);
    }

    @Test
    void containsGroup() {
    }

    @Test
    void addGroup() {
        User aUser = new User("aUser@example.com");

        assertTrue(aUser.addGroup(aGroup));
        assertTrue(aUser.addGroup(bGroup));
    }

    @Test
    void getGroupRoles() {
    }

    @Test
    void addGroupRole() {
    }

    @Test
    void getGroupListMap() {
    }

    @Test
    void isSuperAdmin() {
    }

    @Test
    void getGroups() {
    }

    @Test
    void testGetRoles() {
        User aUser = new User("aUser@example.com");
        aUser.addGroup(aGroup);

        for(GroupRoles.Role role : aUser.getRoles()){
            assertEquals(role, GroupRoles.Role.USER);
        }
    }
}