package com.github.barrettotte.dsl5250.exception

class SystemException extends RuntimeException{

    public SystemException(final String msg){
        super(msg);
    }

    public SystemException(final String msg, final Throwable cause) {
        super(msg, cause);
    }

}