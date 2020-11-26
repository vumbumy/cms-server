package com.cms.repository;

import com.cms.model.User;
import com.cms.model.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long>{
	VerificationCode findByCode(String code);
	Optional<VerificationCode> findByUser(User user);
}