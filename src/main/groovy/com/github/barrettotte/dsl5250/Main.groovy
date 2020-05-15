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

    // Session5250 session

    // static void inputString(final Session5250 session, final String s){
    //     print 'typing \''
    //     for(int i = 0; i < s.length(); i++){
    //         char c = s[i]
    //         print c
    //         session.getScreen().simulateKeyStroke(c)
    //     }
    //     print '\'\n'
    // }

    static void main(String[] args){
        final JsonSlurper slurper = new JsonSlurper()
        final config = slurper.parse(new File(this.getClass().getResource('/config.json').toURI()))
        final Dsl5250 dsl = new Dsl5250()

        dsl.eval{
            environment{
                host = 'PUB400.COM'
                user = 'OTTEB'
            }
            stages{
                stage('LOGIN'){
                    steps{env->
                        position 6,53
                        send "${env.user}"
                        position 7,53
                        send "${env.password}",true
                        waitms 1000
                    }
                }
                stage('TEST'){
                    steps{
                        position 20,7
                        send 'DSPLIBL'
                        cmd 12
                    }
                }
                stage('LOGOFF'){
                    steps{
                        position 20,7
                        send 'SIGNOFF'
                    }
                }
            }
        }
        
        // println "current pos:  ${session.getScreen().getCurrentPos()}"
        // println "current xy:   (${session.getScreen().getCurrentRow()},${session.getScreen().getCurrentCol()})"
        // println "screen size:  ${session.getScreen().getColumns()}x${session.getScreen().getRows()}"

        // println "Moving cursor to (${6},${53}) ..."
        // session.getScreen().setCursor(6, 53)

        // inputString(session, env.user)
    }

}
