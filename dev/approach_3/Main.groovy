package com.github.barrettotte.dsl5250

import com.github.barrettotte.dsl5250.Dsl5250
import com.github.barrettotte.dsl5250.exception.SystemException

import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor

import com.sun.jna.Library
import com.sun.jna.Native
import com.sun.jna.Platform
import com.sun.jna.Pointer
import com.sun.jna.win32.StdCallLibrary

import com.sun.jna.platform.DesktopWindow
import com.sun.jna.platform.KeyboardUtils
import com.sun.jna.platform.WindowUtils
import com.sun.jna.platform.win32.BaseTSD.ULONG_PTR
import com.sun.jna.platform.win32.Kernel32
import com.sun.jna.platform.win32.Kernel32Util
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinDef.DWORD
import com.sun.jna.platform.win32.WinDef.HWND
import com.sun.jna.platform.win32.WinDef.WORD
import com.sun.jna.platform.win32.WinUser
import com.sun.jna.platform.win32.WinUser.INPUT

import groovy.json.JsonSlurper


public class Main{


    // Exit program if guard fails
    static void guard(boolean failCond, final String msg){
        if(failCond){
            final String nativeError = Kernel32Util.formatMessage(Kernel32.INSTANCE.GetLastError())
            println "${msg}\nNative error: ${Native.getLastError()} - ${nativeError}"
            throw new SystemException(nativeError);
        }
    }

    // TODO: search through windows with proc, build list for GUI
    final static String HOST = 'PUB400.COM'
    final static String SESSION = 'A'
    final static String PROCESS = 'acslaunch_win-32.exe'
    

    final static int KEYEVENTF_KEYDOWN = 0
    final static int KEYEVENTF_KEYUP = 2

    final static int VK_RETURN = 0x0D
    final static int VK_TAB = 0x09
    final static int VK_SHIFT = 0x10
    final static int VK_CONTROL = 0x11
    final static int VK_CAPITAL = 0x14
    final static int VK_F5 = 0x74
    final static int VK_12 = 0x7B


    static void inputString(final INPUT input, final String s){
        for(int i = 0; i < s.length(); i++){
            char c = s.charAt(i)
            boolean pressShift = Character.isUpperCase(c)

            if(pressShift){
                sendKeyDown(input, VK_SHIFT)
            }
            sendKey(input, Character.toUpperCase(c) as int)
            if(pressShift){
                sendKeyUp(input, VK_SHIFT)
            }
            Thread.sleep(50)
        }
        println ''
    }

    public static boolean isKeyDown(final int key){
        return (0x1 & (User32.INSTANCE.GetAsyncKeyState(key) >> (Short.SIZE - 1))) != 0 // check most-sig bit for non-zero
    }

    static void sendKeyDown(final INPUT input, final int c){
        input.input.ki.wVk = new WORD(c)
        input.input.ki.dwFlags = new DWORD(KEYEVENTF_KEYDOWN)
        guard(User32.INSTANCE.SendInput(new DWORD(1), (INPUT[]) input.toArray(1), input.size()) == 0, 'send key down failed')
    }

    static void sendKeyUp(final INPUT input, final int c){
        input.input.ki.wVk = new WORD(c)
        input.input.ki.dwFlags = new DWORD(KEYEVENTF_KEYUP)
        guard(User32.INSTANCE.SendInput(new DWORD(1), (INPUT[]) input.toArray(1), input.size()) == 0, 'send key up failed')
    }

    static void sendKey(final INPUT input, final int c){
        sendKeyDown(input, c)
        Thread.sleep(25)
        sendKeyUp(input, c)
    }

    static void simulateInput(){
        INPUT input = new INPUT()
        input.type = new DWORD(INPUT.INPUT_KEYBOARD)
        input.input.setType('ki')
        input.input.ki.wScan = new WORD(0)
        input.input.ki.time = new DWORD(0)
        input.input.ki.dwExtraInfo = new ULONG_PTR(0)

        // TODO: move this elsewhere - probably environment struct
        final jsonSlurper = new JsonSlurper()
        final config = jsonSlurper.parse(new File(this.getClass().getResource('/config.json').toURI()))

        inputString(input, config['user'])
        sendKey(input, VK_TAB)
        inputString(input, config['password'])
        Thread.sleep(500)
        sendKey(input, VK_RETURN)

        Thread.sleep(1000)
        inputString(input, 'DSPUSRPRF ' + config['user'])
        sendKey(input, VK_RETURN)
        Thread.sleep(750)
        sendKey(input, VK_12)

        Thread.sleep(1000)
        inputString(input, 'DSPLIBL')
        sendKey(input, VK_RETURN)
        Thread.sleep(750)
        sendKey(input, VK_12)

        Thread.sleep(1000)
        inputString(input, 'SIGNOFF')
        sendKey(input, VK_RETURN)
    }

    // TODO: after screwing around, move all this junk out of main
    static void main(String[] args){
        println '=====================================              DSL 5250\n'

        final HWND win5250 = User32.INSTANCE.FindWindow(null, "${SESSION} - ${HOST}")
        guard(win5250 == null, 'Error finding 5250 window')
        
        final HWND currentForeground = User32.INSTANCE.GetForegroundWindow()
        final DWORD threadFg = new DWORD(User32.INSTANCE.GetWindowThreadProcessId(currentForeground, null))
        final DWORD thread5250 = new DWORD(User32.INSTANCE.GetWindowThreadProcessId(win5250, null))

        try{
            if(currentForeground != win5250){
                User32.INSTANCE.AttachThreadInput(threadFg, thread5250, true)
                User32.INSTANCE.SetForegroundWindow(win5250)
            }
            guard(User32.INSTANCE.ShowWindow(win5250, User32.SW_SHOWNORMAL) == 0, 'Error showing window (normal)')
            guard(User32.INSTANCE.ShowWindow(win5250, User32.SW_SHOWMAXIMIZED) == 0, 'Error showing window (maximize)')
            guard(User32.INSTANCE.UpdateWindow(win5250) == 0, 'Error updating window')

            //simulateInput()
            
            // TODO: clipboard to text file
            println (Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor) as String)

            guard(User32.INSTANCE.CloseWindow(win5250) == 0, 'Error closing window')

        } catch(final Exception e){
            e.printStackTrace()
        } finally{
            User32.INSTANCE.AttachThreadInput(threadFg, thread5250, false)
        }
    }

}
