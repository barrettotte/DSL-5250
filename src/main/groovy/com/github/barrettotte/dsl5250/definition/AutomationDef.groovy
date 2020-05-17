package com.github.barrettotte.dsl5250.definition

import static groovy.lang.Closure.DELEGATE_FIRST
import static groovy.lang.Closure.DELEGATE_ONLY

import groovy.transform.CompileStatic
import groovy.util.logging.Log4j

import com.github.barrettotte.dsl5250.exception.EnvironmentException
import com.github.barrettotte.dsl5250.model.Environment
import com.github.barrettotte.dsl5250.model.Stage
import com.github.barrettotte.dsl5250.utils.Dsl5250Utils

import org.tn5250j.Session5250
import org.tn5250j.framework.tn5250.Screen5250
import org.tn5250j.framework.common.SessionManager
import org.tn5250j.TN5250jConstants

@Log4j
@CompileStatic
class AutomationDef{

    // properties available throughout DSL
    static Environment env
    static Session5250 session
    static Screen5250 screen
    static Integer screenWidth
    static Integer screenHeight

    static String stageName
    static Integer stageIndex
    static Integer stagesLength
    static Integer stepIndex
    static Integer stepsLength

    void environment(@DelegatesTo(value=Environment, strategy=DELEGATE_FIRST) final Closure closure){
        env = new Environment()
        env.with(closure)
        if(!env.host || !env.user || !env.password){
            throw new EnvironmentException('Invalid environment - must contain host, user, and password')
        }
    }

    void stages(@DelegatesTo(value=StagesDef, strategy=DELEGATE_ONLY) final Closure closure){
        session = connect(env)
        log.info('Session connected')
        
        screen = session.getScreen()
        screenWidth = screen.getColumns()
        screenHeight = screen.getRows()

        final StagesDef dsl = new StagesDef()
        closure.delegate = dsl
        closure.resolveStrategy = DELEGATE_ONLY
        closure.call()

        Boolean hadException = false
        Exception rethrow = null

        stageIndex = 1
        try{
            dsl.stages?.each{stage->
                stageName = stage.name
                stepIndex = 0
                runStage(stage)
                stageIndex++
            }
        } catch(final Exception ex){
            rethrow = ex
            log.info(ex as String)
        } finally{
            session?.disconnect()
            log.info('Session disconnected')
        }
        if(rethrow){
            throw rethrow
        }
    }

    void runStage(final Stage stage){
        final StageDef dsl = new StageDef()

        log.info("==> Running '${stage.name}' stage...")
        stage.closure.delegate = dsl
        stage.closure.resolveStrategy = DELEGATE_ONLY
        stage.closure.call()
    }

    // create 5250 session and establish connection
    private Session5250 connect(final Environment env){
        final Properties props = new Properties()
        props.with{
            put 'SESSION_HOST', env.host
            put 'SESSION_HOST_PORT', env.telnet as String
            put 'SESSION_CODE_PAGE', env.codePage as String
        }
        final Session5250 s = SessionManager.instance().openSession(props, '', '')
        s.connect()

        for(int i = 1; i < 200 && !s.isConnected(); i++){
			Thread.sleep(100)
		}
        Thread.sleep(500)
        return s
    }

}