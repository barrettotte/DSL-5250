package com.github.barrettotte.automation.dsl

import groovy.transform.CompileStatic

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

import com.github.barrettotte.automation.Dsl
import com.github.barrettotte.automation.dsl.StagesDsl
import com.github.barrettotte.automation.model.Environment
import com.github.barrettotte.automation.model.Stage

import static groovy.lang.Closure.DELEGATE_FIRST
import static groovy.lang.Closure.DELEGATE_ONLY


@CompileStatic
class AutomationDsl{
    
    protected static final ConcurrentMap<String, Object> env = [:] as ConcurrentHashMap

    void environment(@DelegatesTo(value=Environment, strategy=DELEGATE_FIRST) final Closure closure){
        env.with(closure)
    }

    void stages(@DelegatesTo(value=StagesDsl, strategy=DELEGATE_ONLY) final Closure closure){
        final StagesDsl dsl = new StagesDsl()

        closure.delegate = dsl
        closure.resolveStrategy = DELEGATE_ONLY
        closure.call()

        dsl.stages.each{
            Dsl.runStage(it)
        }
    }

}
