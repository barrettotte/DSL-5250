package com.github.barrettotte.automation.dsl

import static groovy.lang.Closure.DELEGATE_ONLY

import com.github.barrettotte.automation.model.Stage

import groovy.transform.CompileStatic

@CompileStatic
class Dsl5250{

    static void eval(final File f){
        Dsl5250.eval(f.text)
    }

    static void eval(final String dsl){
        def c = new GroovyShell().evaluate('return ' + dsl) as Closure
        Dsl5250.eval(c)
    }

    static void eval(@DelegatesTo(strategy=Closure.DELEGATE_FIRST, value=AutomationDefinition) final Closure closure){
        final AutomationDefinition automation = new AutomationDefinition()
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure.delegate = automation
        closure.call()
    }

    static void runStage(final Stage stage){
        final StageDefinition dsl = new StageDefinition()

        println "==> Running '${stage.name}' stage..."
        stage.closure.delegate = dsl
        stage.closure.resolveStrategy = DELEGATE_ONLY
        stage.closure.call()
    }

}
