package com.github.barrettotte.dsl5250.exception

class ConnectionException extends RuntimeException{

    ConnectionException(final String msg){
        super(msg)
    }

    ConnectionException(final String msg, final Throwable cause) {
        super(msg, cause)
    }

}