package com.github.barrettotte.dsl5250.model

import groovy.transform.CompileStatic

@CompileStatic
class Environment{
    String host
    String user
    String password
    
    String telnet = '23'
    String codePage = '37'

}
