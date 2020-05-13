package com.github.barrettotte.dsl5250

import com.github.barrettotte.dsl5250.Dsl5250
import com.github.barrettotte.dsl5250.model.Environment

import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor

import groovy.json.JsonSlurper

import org.tn5250j.Session5250
import org.tn5250j.framework.common.SessionManager
import org.tn5250j.Session5250
import org.tn5250j.TN5250jConstants

public class Main{

    Session5250 session

    static void inputString(final Session5250 session, final String s){
        print 'typing \''
        for(int i = 0; i < s.length(); i++){
            char c = s[i]
            print c
            session.getScreen().simulateKeyStroke(c)
        }
        print '\'\n'
    }

    static Session5250 getSession(final Environment env){
        final Properties props = new Properties()
        props.with{
            put 'SESSION_HOST', env.host
            put 'SESSION_HOST_PORT', env.telnet
            put 'SESSION_CODE_PAGE', env.codePage
        }
        final Session5250 session = SessionManager.instance().openSession(props, '', '')
        session.connect()

        for(int i = 1; i < 200 && !session.isConnected(); i++){
			Thread.sleep(100)
		}
        Thread.sleep(500)
        return session
    }


    static void main(String[] args){

        final JsonSlurper slurper = new JsonSlurper()
        final config = slurper.parse(new File(this.getClass().getResource('/config.json').toURI()))

        final Environment env = new Environment()
        env.with{
            host = config['host']
            user = config['user']
            password = config['password']
        }
        final Session5250 session = getSession(env)

        println "current pos:  ${session.getScreen().getCurrentPos()}"
        println "current xy:   (${session.getScreen().getCurrentRow()},${session.getScreen().getCurrentCol()})"
        println "screen size:  ${session.getScreen().getColumns()}x${session.getScreen().getRows()}"

        println "Moving cursor to (${6},${53}) ..."
        session.getScreen().setCursor(6, 53)

        inputString(session, env.user)
        
        session.disconnect()
    }

}
