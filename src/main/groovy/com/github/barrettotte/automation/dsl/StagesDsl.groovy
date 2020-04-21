package com.github.barrettotte.automation.dsl

import groovy.transform.CompileStatic

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

import com.github.barrettotte.automation.model.Stage

import static groovy.lang.Closure.DELEGATE_ONLY


@CompileStatic
class StagesDsl{
    
    protected final List<Stage> stages = []

    void stage(final String name, @DelegatesTo(value=StageDsl, strategy=DELEGATE_ONLY) final Closure closure){
        stages << new Stage(name, closure)
    }

}
