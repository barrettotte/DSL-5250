package com.github.barrettotte.dsl5250

import com.github.barrettotte.dsl5250.Dsl5250
import com.github.barrettotte.dsl5250.nativelib.Ehlapi32

import com.sun.jna.Library
import com.sun.jna.Native
import com.sun.jna.Platform
import com.sun.jna.platform.win32.Kernel32

public class Main{

    
    
    static void main(String[] args){
        println '=====================================              DSL 5250\n'
        println "${System.getProperty('os.arch')} - ${System.getProperty('os.name')}"
        println "Java - ${System.getProperty('java.version')} @ ${System.getProperty('java.home')}"
        
        final Kernel32 kernel32 = Native.loadLibrary('kernel32', Kernel32.class) as Kernel32
        int process = kernel32.GetCurrentProcessId()
        println "process: ${process}"


    }

}
