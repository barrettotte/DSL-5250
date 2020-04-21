package com.github.barrettotte.automation

import com.github.barrettotte.automation.dsl.AutomationDsl
import com.github.barrettotte.automation.dsl.StageDsl
import com.github.barrettotte.automation.model.Stage

import groovy.transform.CompileStatic

import static groovy.lang.Closure.DELEGATE_ONLY


@CompileStatic
class Dsl{

    static void build(@DelegatesTo(strategy=Closure.DELEGATE_FIRST, value=AutomationDsl) final Closure closure){
        final AutomationDsl automation = new AutomationDsl()
        
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure.delegate = automation
        closure.call()
    }

    static void runStage(final Stage stage){
        println "==> Running '${stage.name}' stage..."

        final StageDsl dsl = new StageDsl()
        stage.closure.delegate = dsl
        stage.closure.resolveStrategy = DELEGATE_ONLY
        stage.closure.call()
    }

}
