package com.github.barrettotte.automation.model

class Stage{

    final String name
    final Closure closure

    Stage(final String name, final Closure closure){
        this.name = name
        this.closure = closure
    }

}
