package com.github.barrettotte.automation.dsl

import groovy.transform.CompileStatic

import static groovy.lang.Closure.DELEGATE_ONLY


@CompileStatic
class StepsDsl{

    void position(final int x, final int y){
        println "  - positioning cursor to ($x,$y)"
    }
    
    void send(final String s, final boolean isSensitive=false){
        if(s?.trim() == 0){
            throw new Exception('Cannot send null string to emulator')
        }
        println "  - typing '${(isSensitive) ? ('*' * 16) : s}'"
    }

    void cmd(final int index){
        println "  - pressing F$index"
    }

}
