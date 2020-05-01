package com.github.barrettotte.automation.dsl

import groovy.transform.CompileStatic
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.SimpleType

import com.github.barrettotte.automation.dsl.StageDefinition
import com.github.barrettotte.automation.dsl.StepsDefinition

import static groovy.lang.Closure.DELEGATE_ONLY


@CompileStatic
class StageDefinition{

    void steps(
        @DelegatesTo(value=StepsDefinition, strategy=DELEGATE_ONLY)
        @ClosureParams(value=SimpleType, options=["java.util.Map"]) final Closure closure){

        final StepsDefinition steps = new StepsDefinition()
        
        closure.delegate = steps
        closure.resolveStrategy = DELEGATE_ONLY
        closure.call(AutomationDefinition.env)
    }
}
