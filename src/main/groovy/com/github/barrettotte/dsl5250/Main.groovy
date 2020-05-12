package com.github.barrettotte.dsl5250

import com.github.barrettotte.dsl5250.Dsl5250
import com.github.barrettotte.dsl5250.exception.SystemException

import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor

import groovy.json.JsonSlurper

import org.tn5250j.Session5250
import org.tn5250j.framework.common.SessionManager
import org.tn5250j.Session5250
import org.tn5250j.TN5250jConstants


public class Main{


    static void main(String[] args){

        final String host = 'DEV400'
        final String port = 23
        final String codePage = '37'

        final Properties sessionProperties = new Properties()
        sessionProperties.with{
            put 'SESSION_HOST', host
            put 'SESSION_HOST_PORT', port
            put 'SESSION_CODE_PAGE', codePage
        }

        final Session5250 session = SessionManager.instance().openSession(sessionProperties, '', '')
        session.connect()

        for(int i = 1; i < 200 && !session.isConnected(); i++){
			Thread.sleep(100);
		}
        Thread.sleep(500);
        
        //session.getScreen().dumpScreen()
        //println session.getScreen().getCharacters() as String
        println "current row: ${session.getScreen().getCurrentRow()}"
        println "current col: ${session.getScreen().getCurrentCol()}"
        println "rows: ${session.getScreen().getRows()}"
        println "cols: ${session.getScreen().getCols()}"

        // session.getScreen().setCursor(int row, int col) // offset 1,1
        
        // Session5250.java
        //   int getCurrentRow()
        //   int getCurrentCol()
        //   int getCurrentPos()
        //   int getRow(int pos)
        //   int getCol(int pos)
        //   int getRows()
        //   int getCols()
        //   ScreenOIA getOIA()
        //   ScreenPlanes getPlanes()
        //   boolean simulateMnemonic(int mnem) // get mnemonic constants from TN5250JConstants.java
        //   boolean simulateKeyStroke(char c)
        //   boolean isStatusErrorCode()
        //   int getErrorLine()


        // SessionOIA.java
        // OIA level codes as static ints
        // InputInhibited codes as static ints
        // public boolean isKeyboardLocked()


        session.disconnect()
    }

}
