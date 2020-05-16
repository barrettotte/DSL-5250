package com.github.barrettotte.dsl5250.definition

//import org.tn5250j.keyboard.KeyMnemonic

import groovy.transform.CompileStatic

import com.github.barrettotte.dsl5250.exception.StepException

@CompileStatic
class StepsDef{

    // get cursor position (x,y)
    Map<String, Integer> position(){
        return [x: 0, y: 0] // TODO:
    }

    // set cursor position to x,y (1,1 offset)
    void position(final int x, final int y){
        logStep "positioning cursor to ($x,$y)"
    }

    // get cursor x position
    int postionX(){
        return 0 // TODO:
    }

    // set cursor x position
    void postionX(final int x){
        // TODO:
    }

    // get cursor y postion
    int postionY(){
        return 0 // TODO:
    }

    // set cursor y position
    void postionY(final int y){
        // TODO:
    }

    // Input a string. isSensitive masks string in logging (good for passwords)
    void send(final String s, final boolean isSensitive=false){
        if(s?.trim() == 0){
            throw new StepException('Cannot send null string')
        }
        logStep "typing '${(isSensitive) ? ('*' * 16) : s}'"
    }

    // Get list of strings: for(y to listLength by 1) -> extract(x, (y * 1), bufferLength)
    List<String> extract(final int x, final int y, final int bufferLength, final int listLength){
        return [] // TODO:
        // TODO: edge checks
    }

    // Get list of strings: for(y to listLength by 1) -> extract(x, (y * yIncrement), bufferLength)
    List<String> extract(final int x, final int y, final int bufferLength, final int listLength, final int yIncrement){
        return [] // TODO:
        // TODO: edge checks
    }

    // Extract string from (x,y) to (bufferLength,y)
    String extract(final int x, final int y, final int bufferLength){
        return '' //TODO:
        // TODO: edge checks
    }

    // Extract string from current (x,y) to (bufferLength,y)
    String extract(final int bufferLength){
        return '' //TODO:
        // TODO: edge checks
    }

    // Press F1-F24
    void cmd(final int index){
        if(index < 1 || index > 24){
            throw new StepException("Invalid command key '${index}'.")
        }
        logStep "command $index"
        // sendkeys "[pf${index}]"
    }

    // press a key
    void key(final String key){
        //final KeyMnemonic km = key.toUpperCase()
        logStep "pressing ${key}"
        // TODO: valid key check
    }

    // wait for milliseconds
    void waitms(final int ms){
        try{
            if(ms < 0){
                throw new StepException("Invalid wait time. Cannot wait ${ms} ms.")
            }
            logStep "waiting ${ms} ms..."
            Thread.sleep(ms)
        } catch(final InterruptedException ex){
            //Thread.currentThread().interrupt()
        }
    }

    // log each step
    void logStep(final String s){
        println "    - ${s}"
        println AutomationDef.env
    }

}
