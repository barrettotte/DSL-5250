package com.github.barrettotte.dsl5250.model

import groovy.transform.CompileStatic

@CompileStatic
class Stage{

    final String name
    final Closure closure

    Stage(final String name, final Closure closure){
        this.name = name
        this.closure = closure
    }

}
