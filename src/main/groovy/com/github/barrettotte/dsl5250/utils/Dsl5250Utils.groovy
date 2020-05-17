package com.github.barrettotte.dsl5250.utils

import java.awt.Dimension
import java.nio.file.Paths

class Dsl5250Utils{

    static Closure closureFromFile(final String fileName){
        closureFromFile(new File(fileName))
    }

    static Closure closureFromFile(final URL url){
        closureFromFile(Paths.get(url.toURI()).toFile())
    }

    static Closure closureFromFile(final File f){
        return new GroovyShell().evaluate('return ' + f.text)
    }

    static void waitms(final Integer ms){
        if(ms < 1){
            throw new Exception("Invalid wait time '$ms'ms")
        }
        Thread.sleep(ms)
    }

}