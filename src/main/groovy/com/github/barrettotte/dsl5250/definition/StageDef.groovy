package com.github.barrettotte.dsl5250.definition

import static groovy.lang.Closure.DELEGATE_ONLY

import com.github.barrettotte.dsl5250.Dsl5250

import groovy.transform.CompileStatic
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.SimpleType

@CompileStatic
class StageDef{

    void steps(
        @DelegatesTo(value=StepsDef, strategy=DELEGATE_ONLY)
        @ClosureParams(value=SimpleType, options=['java.util.Map']) final Closure closure){

        final StepsDef steps = new StepsDef()

        closure.delegate = steps
        closure.resolveStrategy = DELEGATE_ONLY
        closure.call(Dsl5250.env)
    }

}