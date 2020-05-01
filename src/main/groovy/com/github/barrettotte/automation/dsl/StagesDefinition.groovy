package com.github.barrettotte.automation.dsl

import static groovy.lang.Closure.DELEGATE_ONLY

import groovy.transform.CompileStatic

import com.github.barrettotte.automation.model.Stage

@CompileStatic
class StagesDefinition{

    protected final List<Stage> stages = []

    void stage(final String name, @DelegatesTo(value=StageDefinition, strategy=DELEGATE_ONLY) final Closure closure){
        stages << new Stage(name, closure)
    }

}