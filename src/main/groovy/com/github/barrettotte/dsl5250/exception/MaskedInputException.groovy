package com.github.barrettotte.dsl5250.exception

class MaskedInputException extends RuntimeException{

    MaskedInputException(final String msg){
        super(msg)
    }

    MaskedInputException(final String msg, final Throwable cause) {
        super(msg, cause)
    }

}