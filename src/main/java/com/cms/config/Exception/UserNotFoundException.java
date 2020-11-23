package com.cms.config.Exception;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserNotFoundException extends UsernameNotFoundException {
    public UserNotFoundException(){
        super("잘못된 사용자입니다.");
    }

    public UserNotFoundException(String msg) {
        super(msg);
    }
}
