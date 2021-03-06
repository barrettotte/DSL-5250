package com.github.barrettotte.dsl5250.model

import groovy.transform.CompileStatic

@CompileStatic
class Environment{

    String host
    String user
    String password

    Integer telnet = 23      // default telnet port
    Integer codePage = 37
    String outputPath = '.'  // output path for screen captures

}