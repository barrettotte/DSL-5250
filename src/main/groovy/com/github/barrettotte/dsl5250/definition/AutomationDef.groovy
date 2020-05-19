package com.github.barrettotte.dsl5250.definition

import static groovy.lang.Closure.DELEGATE_FIRST
import static groovy.lang.Closure.DELEGATE_ONLY

import groovy.transform.CompileStatic
import groovy.util.logging.Log4j

import com.github.barrettotte.dsl5250.Dsl5250
import com.github.barrettotte.dsl5250.model.Environment
import com.github.barrettotte.dsl5250.model.Stage

@Log4j
@CompileStatic
class AutomationDef{

    // load environment closure
    void environment(@DelegatesTo(value=Environment, strategy=DELEGATE_FIRST) final Closure closure){
        Dsl5250.setup(closure)
    }

    // run each stage closure 
    void stages(@DelegatesTo(value=StagesDef, strategy=DELEGATE_ONLY) final Closure closure){
        final StagesDef dsl = new StagesDef()
        closure.delegate = dsl
        closure.resolveStrategy = DELEGATE_ONLY
        closure.call()
        dsl.stages?.each{stage -> Dsl5250.runStage(stage)}
    }

}