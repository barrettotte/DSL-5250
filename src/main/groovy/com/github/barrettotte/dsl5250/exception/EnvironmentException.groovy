package com.github.barrettotte.dsl5250.exception

import groovy.transform.CompileStatic

@CompileStatic
class EnvironmentException extends RuntimeException{

    EnvironmentException(final String msg){
        super(msg)
    }

    EnvironmentException(final String msg, final Throwable cause) {
        super(msg, cause)
    }

}