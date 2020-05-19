package com.github.barrettotte.dsl5250

import static groovy.lang.Closure.DELEGATE_FIRST
import static groovy.lang.Closure.DELEGATE_ONLY

import com.github.barrettotte.dsl5250.definition.AutomationDef
import com.github.barrettotte.dsl5250.definition.StageDef
import com.github.barrettotte.dsl5250.exception.ConnectionException
import com.github.barrettotte.dsl5250.exception.Dsl5250Exception
import com.github.barrettotte.dsl5250.exception.EnvironmentException
import com.github.barrettotte.dsl5250.model.DslState
import com.github.barrettotte.dsl5250.model.Environment
import com.github.barrettotte.dsl5250.model.Stage

import groovy.transform.CompileStatic
import groovy.util.logging.Log4j

import java.nio.file.Paths

import org.codehaus.groovy.runtime.DateGroovyMethods

import org.tn5250j.Session5250
import org.tn5250j.framework.tn5250.Screen5250
import org.tn5250j.framework.common.SessionManager

@Log4j
@CompileStatic
class Dsl5250{

    static Environment env
    static Session5250 session
    static Screen5250 screen
    static DslState state

    static void eval(final File f){
        eval(f.text)
    }

    static void eval(final String dsl){
        def c = new GroovyShell().evaluate('return ' + dsl) as Closure
        eval(c)
    }

    static void eval(@DelegatesTo(strategy=Closure.DELEGATE_FIRST, value=AutomationDef) final Closure closure){
        state = new DslState()
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
        state.stageIndex = 1
    }

    // run stage closure
    static void runStage(final Stage stage){
        log.info("==> Running '${stage.name}' stage...")
        state.stageName = stage.name
        state.stepIndex = 0
        state.stageIndex++

        final StageDef dsl = new StageDef()
        stage.closure.delegate = dsl
        stage.closure.resolveStrategy = DELEGATE_ONLY
        stage.closure.call()
    }

    // processing before each step
    static void preStep(final String s){
        state.stepIndex++
        log.info(s)
    }

    // establish 5250 session connection
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
        state.connected = true
        log.info('Session connected')
    }

    // disconnect from 5250 session
    static void disconnect(){
        session?.disconnect()
        state.connected = false
        log.info('Session disconnected')
    }

    // sleep for ms milliseconds
    static void waitms(final Integer ms){
        if(ms < 1){
            throw new Exception("Invalid wait time '$ms'ms")
        }
        try{
            Thread.sleep(ms)
        } catch (final IllegalMonitorStateException e){
            // just keep going
        }
    }

    // get current row from 5250 screen
    static Integer getCursorRow(){
        return screen.currentRow
    }

    // get current column from 5250 screen
    static Integer getCursorCol(){
        return screen.currentCol
    }

    // get current position (row,col) from 5250 screen
    static Map<String,Integer> getCursorPosition(){
        return [
            row: screen.currentRow,
            col: screen.currentCol,
        ]
    }

    // set 5250 screen cursor position (row,col)
    static void setCursorPosition(final Integer row, final Integer col){
        assertScreenBounds(row, col)
        screen.setCursor(row, col)
    }

    // get 5250 screen height
    static Integer getScreenHeight(){
        return screen.rows
    }

    // get 5250 screen width
    static Integer getScreenWidth(){
        return screen.columns
    }

    // get a string at row,col from screen
    static String scrapeScreen(final Integer row, final Integer col, final Integer len){
        return scrapeScreen()[row - 1].substring(col - 1).take(len)
    }

    // get screen contents split into rows
    static List<String> scrapeScreen(){
        return (screen.screenAsChars as String).split("(?<=\\G.{${screen.columns}})") as List<String>
    }

    //
    static String getDefaultScrapeName(){
        final String today = DateGroovyMethods.format(new Date(), 'yyyyMMdd')
        final String stage = state.stageIndex.toString().padLeft(2, '0') + '_' + state.stageName.replaceAll('\\s', '-')
        return env.outputPath + File.separator + today + "_${stage}_step_${state.stepIndex.toString().padLeft(3, '0')}.txt"
    }

    // enter string in 5250 screen
    static void enterString(final String s){
        if(s?.trim() == 0){
            throwIt 'Cannot send null string'
        }
        screen.sendKeys(s)
    }

    // util to throw a DSL exception
    static void throwIt(final String s){
        throw new Dsl5250Exception("stage '${state.stageName}'; step${state.stepIndex} -> $s")
    }

    // throw exception if position (row,col) is out of bounds for current session's screen
    static void assertScreenBounds(final Integer row, final Integer col){
        if(!row || !col){
            throwIt "Position at $row/$col cannot contain null."
        } else if(col < 1 || col > screen.columns){
            throwIt "Position at $row/$col is out of bounds. Column bounds = [1-${screen.columns}]."
        } else if(row < 1 || row > screen.rows){
            throwIt "Position at $row/$col is out of bounds. Row bounds = [1-${screen.rows}]."
        }
    }

    // save screen contents to file
    static void captureScreen(final String filePath){
        final File f = new File(filePath)
        f.parentFile.mkdirs()
        f.newWriter().withWriter{w ->
            w << scrapeScreen().join('\n')
        }
    }

    // log string s
    static void log(final String s){
        log.info(s)
    }

}