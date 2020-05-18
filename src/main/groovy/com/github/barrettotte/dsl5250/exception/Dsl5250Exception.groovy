package com.github.barrettotte.dsl5250.exception

import groovy.transform.CompileStatic

@CompileStatic
class Dsl5250Exception extends RuntimeException{

    Dsl5250Exception(final String msg){
        super(msg)
    }

    Dsl5250Exception(final String msg, final Throwable cause) {
        super(msg, cause)
    }

}