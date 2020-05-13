package com.github.barrettotte.dsl5250.exception

class StepException extends RuntimeException{

    StepException(final String msg){
        super(msg)
    }

    StepException(final String msg, final Throwable cause) {
        super(msg, cause)
    }

}