package com.github.barrettotte.dsl

import groovy.transform.stc.ClosureParams
import groovy.transform.stc.SimpleType

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

import static groovy.lang.Closure.DELEGATE_FIRST
import static groovy.lang.Closure.DELEGATE_ONLY


class Dsl{

    static void automation(@DelegatesTo(value=AutomationDsl, strategy=DELEGATE_ONLY) final Closure closure){
        final AutomationDsl dsl = new AutomationDsl()

        // closure delegation
        closure.delegate = dsl
        closure.resolveStrategy = Closure.DELEGATE_ONLY // only find on current object, don't look elsewhere
        closure.call()
    }

}


class AutomationDsl{

    static final ConcurrentMap<String, String> env = [:] as ConcurrentHashMap

    void environment(@DelegatesTo(value=Map, strategy=DELEGATE_FIRST) final Closure closure){
        env.with(closure)
    }

    void stages(@DelegatesTo(value=StagesDsl, strategy=DELEGATE_ONLY) final Closure closure){
        final StagesDsl dsl = new StagesDsl()

        closure.delegate = dsl
        closure.resolveStrategy = DELEGATE_ONLY
        closure.call()

        dsl.stages.each{Stage stage ->
            stage.run()
        }
    }
}

class StagesDsl{
    protected final List<Stage> stages = []

    void stage(final String name, @DelegatesTo(value=StageDsl, strategy=DELEGATE_ONLY) final Closure closure){
        stages << new Stage(name, closure)
    }
}

class Stage{
    final String name
    final Closure closure

    Stage(final String name, final Closure closure){
        this.name = name
        this.closure = closure
    }

    void run(){
        println "==> Running '${name}' stage..."
        
        final StageDsl dsl = new StageDsl()
        closure.delegate = dsl
        closure.resolveStrategy = DELEGATE_ONLY
        closure.call()
    }
}

class StageDsl{
    void steps(
        @DelegatesTo(value=Steps, strategy=DELEGATE_ONLY)
        @ClosureParams(value=SimpleType, options=["java.util.Map"]) final Closure closure){

        final Steps steps = new Steps()
        closure.delegate = steps
        closure.resolveStrategy = DELEGATE_ONLY
        closure.call(AutomationDsl.env)
    }
}

class Steps{
    void position(final int x, final int y){
        println "  - positioning cursor to ($x,$y)"
    }
    void send(final String s){
        println "  - typing '$s'"
    }
    void cmd(final int index){
        println "  - pressing F$index"
    }
}
