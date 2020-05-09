package com.github.barrettotte.dsl5250

import com.github.barrettotte.dsl5250.Dsl5250
import com.github.barrettotte.dsl5250.exception.SystemException

import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor

import groovy.json.JsonSlurper

import org.tn5250j.Session5250
import org.tn5250j.framework.common.SessionManager


public class Main{

    // TODO: after screwing around, move all this junk out of main
    static void main(String[] args){

        final String host = 'PUB400.COM'
        final String port = 2222
        final String codePage = '37'

        final Properties sessionProperties = new Properties()
        sessionProperties.with{
            put 'HOST', host
            put 'PORT', port
            put 'CODEPAGE', codePage
        }
        final Session5250 session = SessionManager.instance().openSession(sessionProperties, '', '')
        println session.isConnected()
        session.connect()
        println session.isConnected()

        session.disconnect()
    }

}
