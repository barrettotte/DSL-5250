package com.github.barrettotte.dsl5250

import static groovy.lang.Closure.DELEGATE_FIRST

import com.github.barrettotte.dsl5250.definition.AutomationDef
import com.github.barrettotte.dsl5250.exception.ConnectionException
import com.github.barrettotte.dsl5250.exception.EnvironmentException
import com.github.barrettotte.dsl5250.model.Environment

import groovy.transform.CompileStatic
import groovy.util.logging.Log4j

import org.tn5250j.Session5250
import org.tn5250j.framework.tn5250.Screen5250
import org.tn5250j.framework.common.SessionManager

@Log4j
@CompileStatic
class Dsl5250{

    // properties available throughout DSL
    static Environment env
    static Session5250 session
    static Screen5250 screen
    static Integer screenWidth = 0
    static Integer screenHeight = 0

    static String  stageName = ''
    static Integer stageIndex = 0
    static Integer stagesLength = 0
    static Integer stepIndex = 0
    static Integer stepsLength = 0


    static void eval(final File f){
        eval(f.text)
    }

    static void eval(final String dsl){
        def c = new GroovyShell().evaluate('return ' + dsl) as Closure
        eval(c)
    }

    static void eval(@DelegatesTo(strategy=Closure.DELEGATE_FIRST, value=AutomationDef) final Closure closure){
        Exception rethrow = null
        try{
            final AutomationDef automation = new AutomationDef()
            closure.resolveStrategy = Closure.DELEGATE_FIRST
            closure.delegate = automation
            closure.call()
        } catch (final Exception ex){
            rethrow = ex
            log.info(ex as String)
        } finally{
            disconnect()
        }
        if(rethrow) throw rethrow
    }

    // setup environment and connection
    static void setup(@DelegatesTo(value=Environment, strategy=DELEGATE_FIRST) final Closure closure){
        env = new Environment()
        env.with(closure)
        if(!env.host || !env.user || !env.password){
            throw new EnvironmentException('Invalid environment - must contain host, user, and password')
        }
        connect()

        screen = session.screen
        screenWidth = screen.columns
        screenHeight = screen.rows
        stageIndex = 1
    }

    // create 5250 session and establish connection
    static void connect(){
        final Properties props = new Properties()
        props.with{
            put 'SESSION_HOST', env.host
            put 'SESSION_HOST_PORT', env.telnet as String
            put 'SESSION_CODE_PAGE', env.codePage as String
        }
        session = SessionManager.instance().openSession(props, '', '')
        session.connect()

        for (int i = 1; i < 200 && !session.connected; i++){
			Thread.sleep(100)
		}
        Thread.sleep(500)

        if(!session.connected){
            throw new ConnectionException('Could not connect to 5250 session')
        }
        log.info('Session connected')
    }

    // disconnect from 5250 session
    static void disconnect(){
        session?.disconnect()
        log.info('Session disconnected')
    }

}