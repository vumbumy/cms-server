package com.cms.repository;

import com.cms.model.Content;
import com.cms.model.Permission;
import com.cms.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
//    Boolean existsByUserAndAccessGreaterThanEqual(User user, Permission.Access access);
}