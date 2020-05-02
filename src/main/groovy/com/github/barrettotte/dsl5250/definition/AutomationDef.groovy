package com.github.barrettotte.dsl5250.definition

import static groovy.lang.Closure.DELEGATE_FIRST
import static groovy.lang.Closure.DELEGATE_ONLY

import groovy.transform.CompileStatic

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

import com.github.barrettotte.dsl5250.model.Environment
import com.github.barrettotte.dsl5250.model.Stage


@CompileStatic
class AutomationDef{

    protected static final ConcurrentMap<String, Object> env = [:] as ConcurrentHashMap

    void environment(@DelegatesTo(value=Environment, strategy=DELEGATE_FIRST) final Closure closure){
        env.with(closure)
    }

    void stages(@DelegatesTo(value=StagesDef, strategy=DELEGATE_ONLY) final Closure closure){
        final StagesDef dsl = new StagesDef()

        closure.delegate = dsl
        closure.resolveStrategy = DELEGATE_ONLY
        closure.call()

        dsl.stages.each{stage->
            runStage(stage)
        }
    }

    void runStage(final Stage stage){
        final StageDef dsl = new StageDef()

        println "==> Running '${stage.name}' stage..."
        stage.closure.delegate = dsl
        stage.closure.resolveStrategy = DELEGATE_ONLY
        stage.closure.call()
    }

}
