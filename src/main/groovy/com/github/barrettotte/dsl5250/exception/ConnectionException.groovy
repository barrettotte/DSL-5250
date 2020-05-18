package com.github.barrettotte.dsl5250.exception

import groovy.transform.CompileStatic

@CompileStatic
class ConnectionException extends RuntimeException{

    ConnectionException(final String msg){
        super(msg)
    }

    ConnectionException(final String msg, final Throwable cause) {
        super(msg, cause)
    }

}