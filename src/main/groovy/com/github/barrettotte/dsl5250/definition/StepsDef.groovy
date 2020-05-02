package com.github.barrettotte.dsl5250.definition

import groovy.transform.CompileStatic

@CompileStatic
class StepsDef{

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
            Thread.sleep(ms)
        } catch(final InterruptedException ex){
            Thread.currentThread().interrupt()
        }
    }

    void printStep(final String s){
        println "    - ${s}"
    }

}
