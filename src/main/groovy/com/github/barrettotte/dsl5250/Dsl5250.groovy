package com.github.barrettotte.dsl5250

import static groovy.lang.Closure.DELEGATE_ONLY

import com.github.barrettotte.dsl5250.definition.AutomationDef
import com.github.barrettotte.dsl5250.nativelib.Ehlapi32

import com.sun.jna.Native

import groovy.transform.CompileStatic

@CompileStatic
class Dsl5250{

    //private Ehlapi32 ehlapi32

    Dsl5250(){
        //Ehlapi32.INSTANCE.wd_ConnectPS(int hInstance, String shortName)
        //System.setProperty('jna.library.path', 'C:/Program Files (x86)/IBM/EHLLAPI/')
        //this.ehlapi32 = (Ehlapi32) Native.loadLibrary('enlapi32', Ehlapi32.class)
    }
    
    void eval(final File f){
        eval(f.text)
    }

    void eval(final String dsl){
        def c = new GroovyShell().evaluate('return ' + dsl) as Closure
        eval(c)
    }

    void eval(@DelegatesTo(strategy=Closure.DELEGATE_FIRST, value=AutomationDef) final Closure closure){
        final AutomationDef automation = new AutomationDef()
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure.delegate = automation
        closure.call()
    }

}
