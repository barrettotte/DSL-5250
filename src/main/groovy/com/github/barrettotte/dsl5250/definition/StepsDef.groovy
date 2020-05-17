package com.github.barrettotte.dsl5250.definition

import org.tn5250j.Session5250
import org.tn5250j.framework.tn5250.Screen5250

import com.github.barrettotte.dsl5250.constant.Key
import com.github.barrettotte.dsl5250.exception.StepException
import com.github.barrettotte.dsl5250.model.Environment
import com.github.barrettotte.dsl5250.utils.Dsl5250Utils

import groovy.transform.CompileStatic
import groovy.util.logging.Log4j

import org.codehaus.groovy.runtime.DateGroovyMethods

@Log4j
@CompileStatic
class StepsDef{

    // TODO: move to utils ?
    // processing before each step
    void preStep(final String s){
        AutomationDef.stepIndex++
        log.info(s)
    }
    // preStep, postStep ?
    // make a model like how Stage works ?

    // get current row
    Integer row(){
        preStep 'Fetching current row'
        return AutomationDef.screen.getCurrentRow()
    }

    // get current column
    Integer col(){
        preStep 'Fetching current column'
        return AutomationDef.screen.getCurrentCol()
    }

    // get cursor position as (row,col)
    Map<String, Integer> position(){
        preStep "Fetching cursor position"
        return [
            row: AutomationDef.screen.getCurrentRow(), 
            col: AutomationDef.screen.getCurrentCol()
        ]
    }

    // set current row
    void row(final Integer row){
        position(row, AutomationDef.screen.getCurrentCol())
    }

    // set current column
    void col(final Integer col){
        position(AutomationDef.screen.getCurrentRow(), col)
    }

    // set cursor position to row,col (1,1 offset)
    void position(final Integer row, final Integer col){
        assertScreenBounds(row, col)
        preStep "Positioning cursor to $row/$col"
        AutomationDef.screen.setCursor(row, col)
    }

    // get number of rows on screen
    Integer height(){
        preStep 'Fetching screen height'
        return AutomationDef.screenHeight
    }

    // get number of columns on screen
    Integer width(){
        preStep 'Fetching screen width'
        return AutomationDef.screenWidth
    }

    // reposition cursor and input string s
    void send(final int row, final int col, final String s, final Boolean isSensitive=false){
        position(row, col)
        Dsl5250Utils.waitms(250)
        send(s, isSensitive)
    }

    // input integer
    void send(final Integer i){
        send "$i"
    }

    // input string. isSensitive masks string in logging (good for secrets)
    void send(final String s, final Boolean isSensitive=false){
        if(s?.trim() == 0){
            throwIt 'Cannot send null string'
        }
        preStep "typing '${(isSensitive) ? ('*' * 16) : s}'"
        AutomationDef.screen.sendKeys(s)
    }

    // get list of strings - incrementing row by 1
    List<String> extract(final Integer row, final Integer col, final Integer bufferLength, final Integer listLength){
        extract(row, col, bufferLength, listLength, 1)
    }

    // get list of strings
    List<String> extract(final Integer row, final Integer col, final Integer bufferLength, final Integer listLength, final Integer rowIncrement){
        List<String> l = []
        preStep "Extracting list of strings with length $bufferLength, from positions $row/$col to ${row + (listLength * rowIncrement)}/$col"
        return [] // TODO:
        // TODO: edge checks
    }

    // extract string from current (row,col) to (row,col+bufferLength)
    String extract(final Integer bufferLength){
        extract(AutomationDef.screen.getCurrentRow(),  AutomationDef.screen.getCurrentCol(), bufferLength)
    }

    // extract string from (row,col) to (bufferLength,y)
    String extract(final Integer row, final Integer col, final Integer bufferLength){
        String s = ''
        preStep "Extracting string of length $bufferLength from position $row/$col"
        return getScreenData(row, col, bufferLength)
        // TODO: edge checks
        return s
    }

    // perform command -> F1-F24
    void cmd(final Integer i){
        if(i < 1 || i > 24){
            throwIt "Invalid command key '$i'."
        }
        preStep "Command $i (F$i)"
        AutomationDef.screen.sendKeys("[pf$i]")
        Dsl5250Utils.waitms(500)
    }

    // press a key
    void press(final Key key){
        preStep "Pressing $key key"
        AutomationDef.screen.sendKeys(key.code)
        Dsl5250Utils.waitms(500)
    }

    // press a key - by string instead of enum
    void press(final String s){
        final String lookup = s.toLowerCase()
        final List<String> codes = Key.values()*.code
        if(!(lookup in codes)){
            throwIt "Invalid key '$s'.\n  Valid: $codes"
        }
        press(s as Key)
    }

    // wait for milliseconds
    void wait(final Integer ms){
        preStep "Waiting $ms ms..."
        Dsl5250Utils.waitms(ms)
    }

    // wait until string appears at specified position row/col or timeout
    void waitUntil(final String s, final Integer row, final Integer col, final Integer timeout=1000, final Integer retry=3){
        preStep "Waiting for '$s' to appear at position $row/$col"
        int i = 0
        boolean foundIt = false
        while(i < retry && !foundIt){
            if(check(s, row, col)){
                foundIt = true
            }
            Dsl5250Utils.waitms(timeout)
            i++
        }
        if(!foundIt){
            throw new StepException("Timed out $retry time(s) waiting to see '${s}' at position $row/$col")
        }
    }

    // check string exists at position row/col
    Boolean check(final String expected, final Integer row, final Integer col){
        preStep "Checking if '$expected' exists at position $row/$col to $row/${col + expected.length()}"
        return expected == getScreenData(row, col, expected.length())
    }

    // write screen contents to file, using default naming convention
    void capture(){
        final String today = DateGroovyMethods.format(new Date(), 'yyyyMMdd')
        final String stage = AutomationDef.stageIndex.toString().padLeft(2,'0') + '_' + AutomationDef.stageName.replaceAll('\\s','-')
        capture(AutomationDef.env.outputPath + File.separator + today + "_${stage}_step_${AutomationDef.stepIndex.toString().padLeft(3,'0')}.txt")
    }

    // write screen contents to file at file path
    void capture(final String filePath){
        final File f = new File(filePath)
        f.getParentFile().mkdirs()
        f.newWriter().withWriter{w->
            w << screenContents().join('\n')
        }
    }

    // TODO: move to utils ?
    // get a string at row,col from screen contents
    String getScreenData(final Integer row, final Integer col, final Integer len){
        return screenContents()[row-1].substring(col-1).take(len)
    }

    // TODO: move to utils ?
    // get screen contents split into rows
    List<String> screenContents(){
        return (AutomationDef.screen.getScreenAsChars() as String).split("(?<=\\G.{${AutomationDef.screenWidth}})") as List<String>
    }

    // TODO: move to utils ?
    // wrapper to throw a StepException
    void throwIt(final String s){ 
        throw new StepException("stage '${AutomationDef.stageName}'; step${AutomationDef.stepIndex} -> $s")
    }

    // TODO: move to utils ?
    // throw exception if position (row,col) is out of bounds for current session's screen
    void assertScreenBounds(final Integer row, final Integer col){
        if(!row || !col){
            throwIt "Cannot position to $row/$col. Position cannot contain null."
        } else if(col < 1 || col > AutomationDef.screenWidth){
            throwIt "Cannot position to $row/$col. Column position out of bounds [1-${AutomationDef.screenWidth}]."
        } else if(row < 1 || row > AutomationDef.screenHeight){
            throwIt "Cannot position to $row/$col. Row position out of bounds [1-${AutomationDef.screenHeight}]."
        }
    }

}
