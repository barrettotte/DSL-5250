package com.github.barrettotte.automation.dsl

import groovy.transform.CompileStatic
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.SimpleType

import com.github.barrettotte.automation.dsl.StageDsl
import com.github.barrettotte.automation.dsl.StepsDsl

import static groovy.lang.Closure.DELEGATE_ONLY


@CompileStatic
class StageDsl{

    void steps(
        @DelegatesTo(value=StepsDsl, strategy=DELEGATE_ONLY)
        @ClosureParams(value=SimpleType, options=["java.util.Map"]) final Closure closure){

        final StepsDsl steps = new StepsDsl()
        
        closure.delegate = steps
        closure.resolveStrategy = DELEGATE_ONLY
        closure.call(AutomationDsl.env)
    }
}
