package com.cms.service;

import com.cms.config.dto.MailDTO;
import com.cms.model.User;
import com.cms.model.VerificationCode;
import com.cms.repository.VerificationCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class VerificationService {
    @Autowired
    VerificationCodeRepository tokenRepository;

    @Autowired
    MailService mailService;

    public Long sendVerificationCodeEmail(User user) {
        VerificationCode token = createVerificationCode(user);

        String message = "CODE: " + token.getCode();

        MailDTO mailDTO = new MailDTO(user.getEmail(), "JOIN", message);

        mailService.mailSend(mailDTO);

        return user.getId();
    }

    public VerificationCode createVerificationCode(User user) {
        String token = Integer.toHexString(UUID.randomUUID().hashCode());

        Optional<VerificationCode> oldCode = tokenRepository.findByUser(user);
        oldCode.ifPresent(thisCode -> tokenRepository.delete(thisCode));

        VerificationCode newCode = new VerificationCode(token, user);

        return tokenRepository.save(newCode);
    }

    public User verifyCode(String token){
        VerificationCode verificationCode = tokenRepository.findByCode(token);
        if (verificationCode == null) {
            throw new RuntimeException("invalid Token");
        }

        return verificationCode.getUser();
    }
}
