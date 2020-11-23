package com.cms.config.Exception;

import lombok.NoArgsConstructor;

public class NullUserException extends RuntimeException {
    public NullUserException(){
        super("잘못된 사용자입니다.");
    }
    public NullUserException(String var1) {
        super(var1);
    }
}