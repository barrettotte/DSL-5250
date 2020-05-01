package com.github.barrettotte.automation.dsl

import groovy.transform.CompileStatic

import static groovy.lang.Closure.DELEGATE_ONLY


@CompileStatic
class StepsDefinition{

    void position(final int x, final int y){
        printStep "positioning cursor to ($x,$y)"
    }
    
    void send(final String s, final boolean isSensitive=false){
        if(s?.trim() == 0){
            throw new Exception('Cannot send null string to emulator')
        }
        printStep "typing '${(isSensitive) ? ('*' * 16) : s}'"
    }

    void cmd(final int index){
        printStep "pressing F$index"
    }

    void waitms(final int ms){
        try{
            printStep "waiting ${ms} ms..."
            Thread.sleep(ms);
        }
        catch(final InterruptedException ex){
            Thread.currentThread().interrupt();
        }
    }

    void printStep(final String s){
        println "    - ${s}"
    }

}
