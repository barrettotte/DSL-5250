package com.github.barrettotte.dsl5250.definition

import com.github.barrettotte.dsl5250.constant.Key
import com.github.barrettotte.dsl5250.exception.Dsl5250Exception
import com.github.barrettotte.dsl5250.utils.Dsl5250Utils

import groovy.transform.CompileStatic

@CompileStatic
class StepsDef{

    // get current row
    Integer row(){
        Dsl5250Utils.preStep 'Fetching current row'
        return Dsl5250Utils.getCursorRow()
    }

    // get current column
    Integer col(){
        Dsl5250Utils.preStep 'Fetching current column'
        return Dsl5250Utils.getCursorCol()
    }

    // get cursor position as (row,col)
    Map<String, Integer> position(){
        Dsl5250Utils.preStep "Fetching cursor position"
        return Dsl5250Utils.getCursorPosition()
    }

    // set current row
    void row(final Integer row){
        position(row, Dsl5250Utils.getCursorCol())
    }

    // set current column
    void col(final Integer col){
        position(Dsl5250Utils.getCursorRow(), col)
    }

    // set cursor position to row,col (1,1 offset)
    void position(final Integer row, final Integer col){
        Dsl5250Utils.preStep "Positioning cursor to $row/$col"
        Dsl5250Utils.setCursorPosition(row, col)
    }

    // get number of rows on screen
    Integer height(){
        Dsl5250Utils.preStep 'Fetching screen height'
        return Dsl5250Utils.getScreenHeight()
    }

    // get number of columns on screen
    Integer width(){
        Dsl5250Utils.preStep 'Fetching screen width'
        return Dsl5250Utils.getScreenWidth()
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
        Dsl5250Utils.preStep "typing '${(isSensitive) ? ('*' * 16) : s}'"
        Dsl5250Utils.enterString(s)
    }

    // get list of strings - incrementing row by 1
    List<String> extract(final Integer row, final Integer col, final Integer bufferLength, final Integer listLength){
        extract(row, col, bufferLength, listLength, 1)
    }

    // get list of strings
    List<String> extract(final Integer row, final Integer col, final Integer bufferLength, final Integer listLength, final Integer rowIncrement){
        Dsl5250Utils.preStep "Extracting list of strings with length $bufferLength, from positions $row/$col to ${row + (listLength * rowIncrement)}/$col"
        List<String> data = []
        for(int i = 0; i < listLength; i++){
            data << extract(row + (i * rowIncrement), col, bufferLength)
        }
        return data
    }

    // extract string from current (row,col) to (row,col+bufferLength)
    String extract(final Integer bufferLength){
        extract(Dsl5250Utils.getCursorRow(), Dsl5250Utils.getCursorCol(), bufferLength)
    }

    // extract string from (row,col) to (bufferLength,y)
    String extract(final Integer row, final Integer col, final Integer bufferLength){
        Dsl5250Utils.preStep "Extracting string of length $bufferLength from position $row/$col"
        Dsl5250Utils.assertScreenBounds(row, col)
        return Dsl5250Utils.scrapeScreen(row, col, bufferLength)
    }

    // perform command -> F1-F24
    void cmd(final Integer i){
        if(i < 1 || i > 24){
            Dsl5250Utils.throwIt "Invalid command key '$i'."
        }
        Dsl5250Utils.preStep "Command $i (F$i)"
        Dsl5250Utils.enterString("[pf$i]")
        Dsl5250Utils.waitms(500)
    }

    // press a key
    void press(final Key key){
        Dsl5250Utils.preStep "Pressing $key key"
        Dsl5250Utils.enterString(key.code)
        Dsl5250Utils.waitms(500)
    }

    // press a key - by string instead of enum
    void press(final String s){
        final List<String> codes = Key.values()*.code
        if(!(s.toLowerCase() in codes)){
            Dsl5250Utils.throwIt "Invalid key '$s'.\n  Valid: $codes"
        }
        press(s as Key)
    }

    // wait for milliseconds
    void waitms(final Integer ms){
        Dsl5250Utils.preStep "Waiting $ms ms..."
        Dsl5250Utils.waitms(ms)
    }

    // wait until string appears at specified position row/col or timeout
    String waitUntil(final String s, final Integer row, final Integer col, final Integer timeout=1000, final Integer retry=3){
        Dsl5250Utils.preStep "Waiting for '$s' to appear at position $row/$col"
        for(int i = 0; i < retry; i++){
            if(check(s, row, col)){
                return s
            }
            Dsl5250Utils.waitms(timeout)
        }
        throw new Dsl5250Exception("Timed out $retry time(s) waiting to see '${s}' at position $row/$col")
    }

    // check string exists at position row/col
    Boolean check(final String expected, final Integer row, final Integer col){
        Dsl5250Utils.preStep "Checking if '$expected' exists at position $row/$col to $row/${col + expected.length()}"
        Dsl5250Utils.assertScreenBounds(row, col + expected.length())
        return expected == Dsl5250Utils.scrapeScreen(row, col, expected.length())
    }

    // write screen contents to file, using default naming convention
    void capture(){
        capture(Dsl5250Utils.getDefaultScrapeName())
    }

    // write screen contents to file at file path
    void capture(final String filePath){
        Dsl5250Utils.preStep "Saving screen contents to '$filePath'"
        Dsl5250Utils.captureScreen(filePath)
    }

}
