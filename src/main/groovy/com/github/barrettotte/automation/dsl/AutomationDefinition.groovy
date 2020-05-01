package com.github.barrettotte.automation.dsl

import static groovy.lang.Closure.DELEGATE_FIRST
import static groovy.lang.Closure.DELEGATE_ONLY

import groovy.transform.CompileStatic

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

import com.github.barrettotte.automation.model.Environment

@CompileStatic
class AutomationDefinition{

    protected static final ConcurrentMap<String, Object> env = [:] as ConcurrentHashMap

    void environment(@DelegatesTo(value=Environment, strategy=DELEGATE_FIRST) final Closure closure){
        env.with(closure)
    }

    void stages(@DelegatesTo(value=StagesDefinition, strategy=DELEGATE_ONLY) final Closure closure){
        final StagesDefinition dsl = new StagesDefinition()

        closure.delegate = dsl
        closure.resolveStrategy = DELEGATE_ONLY
        closure.call()

        dsl.stages.each{stage->
            Dsl5250.runStage(stage)
        }
    }

}
