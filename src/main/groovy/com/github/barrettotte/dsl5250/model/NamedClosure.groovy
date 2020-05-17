package com.github.barrettotte.dsl5250.model

import groovy.transform.CompileStatic

@CompileStatic
class NamedClosure{

    final String name
    final Closure closure

    NamedClosure(final String name, final Closure closure){
        this.name = name
        this.closure = closure
    }

}
