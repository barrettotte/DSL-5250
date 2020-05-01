package com.github.barrettotte.automation.model

import groovy.transform.CompileStatic

@CompileStatic
class Environment{

    final String system
    final String username
    final String password

    Environment(final String system, final String username, final String password){
        this.system = system
        this.username = username
        this.password = password
    }

}
