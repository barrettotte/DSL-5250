package com.github.barrettotte.dsl5250.definition

import static groovy.lang.Closure.DELEGATE_FIRST
import static groovy.lang.Closure.DELEGATE_ONLY

import groovy.transform.CompileStatic

import com.github.barrettotte.dsl5250.exception.EnvironmentException
import com.github.barrettotte.dsl5250.model.Environment
import com.github.barrettotte.dsl5250.model.Stage
import com.github.barrettotte.dsl5250.utils.Dsl5250Utils

import org.tn5250j.Session5250
import org.tn5250j.framework.common.SessionManager
import org.tn5250j.Session5250
import org.tn5250j.TN5250jConstants

@CompileStatic
class AutomationDef{

    static Environment env
    static Session5250 session

    void environment(@DelegatesTo(value=Environment, strategy=DELEGATE_FIRST) final Closure closure){
        env = new Environment()
        env.with(closure)
        if(!env.host || !env.user || !env.password){
            throw new EnvironmentException('Invalid environment - must contain host, user, and password')
        }
        session = connect(env)
    }

    void stages(@DelegatesTo(value=StagesDef, strategy=DELEGATE_ONLY) final Closure closure){
        final StagesDef dsl = new StagesDef()

        closure.delegate = dsl
        closure.resolveStrategy = DELEGATE_ONLY
        closure.call()

        dsl.stages?.each{stage->
            runStage(stage)
        }
        session?.disconnect()
    }

    void runStage(final Stage stage){
        final StageDef dsl = new StageDef()

        println "==> Running '${stage.name}' stage..."
        stage.closure.delegate = dsl
        stage.closure.resolveStrategy = DELEGATE_ONLY
        stage.closure.call()
    }

    // create 5250 session and establish connection
    private Session5250 connect(final Environment env){
        final Properties props = new Properties()
        props.with{
            put 'SESSION_HOST', env.host
            put 'SESSION_HOST_PORT', env.telnet
            put 'SESSION_CODE_PAGE', env.codePage
        }
        final Session5250 sess = SessionManager.instance().openSession(props, '', '')
        sess.connect()

        for(int i = 1; i < 200 && !sess.isConnected(); i++){
			Thread.sleep(100)
		}
        Thread.sleep(500)
        return sess
    }

}