package com.github.barrettotte.dsl5250.utils

import com.github.barrettotte.dsl5250.Dsl5250
import com.github.barrettotte.dsl5250.definition.AutomationDef
import com.github.barrettotte.dsl5250.exception.Dsl5250Exception

import java.nio.file.Paths

import groovy.util.logging.Log4j

import org.codehaus.groovy.runtime.DateGroovyMethods

@Log4j
class Dsl5250Utils{

    static Closure closureFromFile(final String fileName){
        closureFromFile(new File(fileName))
    }

    static Closure closureFromFile(final URL url){
        closureFromFile(Paths.get(url.toURI()).toFile())
    }

    static Closure closureFromFile(final File f){
        return new GroovyShell().evaluate('return ' + f.text)
    }

    // processing before each step -> TODO: i dont like this...what to do...?
    static void preStep(final String s){
        Dsl5250.stepIndex++
        Dsl5250Utils.log(s)
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
        return Dsl5250.screen.currentRow
    }

    // get current column from 5250 screen
    static Integer getCursorCol(){
        return Dsl5250.screen.currentCol
    }

    // get current position (row,col) from 5250 screen
    static Map<String,Integer> getCursorPosition(){
        return [
            row: Dsl5250.screen.currentRow,
            col: Dsl5250.screen.currentCol,
        ]
    }

    // set 5250 screen cursor position (row,col)
    static void setCursorPosition(final Integer row, final Integer col){
        assertScreenBounds(row, col)
        Dsl5250.screen.setCursor(row, col)
    }

    // get 5250 screen height
    static Integer getScreenHeight(){
        return Dsl5250.screenHeight
    }

    // get 5250 screen width
    static Integer getScreenWidth(){
        return Dsl5250.screenWidth
    }

    // get a string at row,col from screen
    static String scrapeScreen(final Integer row, final Integer col, final Integer len){
        return scrapeScreen()[row - 1].substring(col - 1).take(len)
    }

    // get screen contents split into rows
    static List<String> scrapeScreen(){
        return (Dsl5250.screen.screenAsChars as String).split("(?<=\\G.{${Dsl5250.screenWidth}})") as List<String>
    }

    //
    static String getDefaultScrapeName(){
        final String today = DateGroovyMethods.format(new Date(), 'yyyyMMdd')
        final String stage = Dsl5250.stageIndex.toString().padLeft(2, '0') + '_' + Dsl5250.stageName.replaceAll('\\s', '-')
        return Dsl5250.env.outputPath + File.separator + today + "_${stage}_step_${Dsl5250.stepIndex.toString().padLeft(3, '0')}.txt"
    }

    // enter string in 5250 screen
    static void enterString(final String s){
        if(s?.trim() == 0){
            throwIt 'Cannot send null string'
        }
        Dsl5250.screen.sendKeys(s)
    }

    // util to throw a DSL exception
    static void throwIt(final String s){
        throw new Dsl5250Exception("stage '${Dsl5250.stageName}'; step${Dsl5250.stepIndex} -> $s")
    }

    // throw exception if position (row,col) is out of bounds for current session's screen
    static void assertScreenBounds(final Integer row, final Integer col){
        if(!row || !col){
            throwIt "Position at $row/$col cannot contain null."
        } else if(col < 1 || col > Dsl5250.screenWidth){
            throwIt "Position at $row/$col is out of bounds. Column bounds = [1-${Dsl5250.screenWidth}]."
        } else if(row < 1 || row > Dsl5250.screenHeight){
            throwIt "Position at $row/$col is out of bounds. Row bounds = [1-${Dsl5250.screenHeight}]."
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