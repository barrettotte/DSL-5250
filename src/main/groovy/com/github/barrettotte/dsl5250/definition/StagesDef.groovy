package com.github.barrettotte.dsl5250.definition

import static groovy.lang.Closure.DELEGATE_ONLY

import groovy.transform.CompileStatic

import com.github.barrettotte.dsl5250.model.Stage

@CompileStatic
class StagesDef{

    protected final List<Stage> stages = []

    void stage(final String name, @DelegatesTo(value=StageDef, strategy=DELEGATE_ONLY) final Closure closure){
        stages << new Stage(name, closure)
    }

}