package com.github.barrettotte.dsl5250.definition

//import org.tn5250j.keyboard.KeyMnemonic

import groovy.transform.CompileStatic

import com.github.barrettotte.dsl5250.exception.StepException

@CompileStatic
class StepsDef{

    void position(final int x, final int y){
        logStep "positioning cursor to ($x,$y)"
    }

    void send(final String s, final boolean isSensitive=false){
        if(s?.trim() == 0){
            throw new StepException('Cannot send null string')
        }
        logStep "typing '${(isSensitive) ? ('*' * 16) : s}'"
    }

    void cmd(final int index){
        if(index < 1 || index > 24){
            throw new StepException("Invalid command key '${index}'.")
        }
        logStep "command $index"
        // sendkeys "[pf${index}]"
    }

    void key(final String key){
        //final KeyMnemonic km = key.toUpperCase()
        logStep "pressing "
    }

    void waitms(final int ms){
        try{
            logStep "waiting ${ms} ms..."
            Thread.sleep(ms)
        } catch(final InterruptedException ex){
            //Thread.currentThread().interrupt()
        }
    }

    void logStep(final String s){
        println "    - ${s}"
    }

}
