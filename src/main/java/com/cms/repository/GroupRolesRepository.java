package com.cms.repository;

import com.cms.model.GroupRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRolesRepository extends JpaRepository<GroupRoles, Long> {}