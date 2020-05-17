package com.github.barrettotte.dsl5250

import static groovy.lang.Closure.DELEGATE_ONLY

import com.github.barrettotte.dsl5250.definition.AutomationDef

import groovy.transform.CompileStatic

@CompileStatic
class Dsl5250{
    
    static void eval(final File f){
        eval(f.text)
    }

    static void eval(final String dsl){
        def c = new GroovyShell().evaluate('return ' + dsl) as Closure
        eval(c)
    }

    static void eval(@DelegatesTo(strategy=Closure.DELEGATE_FIRST, value=AutomationDef) final Closure closure){
        final AutomationDef automation = new AutomationDef()
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure.delegate = automation
        closure.call()
    }

}
