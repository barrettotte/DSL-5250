package com.github.barrettotte.dsl5250

import com.github.barrettotte.dsl5250.Dsl5250
import com.github.barrettotte.dsl5250.exception.SystemException
import com.github.barrettotte.dsl5250.nativelib.Ehlapi32

import com.sun.jna.Library
import com.sun.jna.Native
import com.sun.jna.Platform
import com.sun.jna.Pointer
import com.sun.jna.win32.StdCallLibrary

import com.sun.jna.platform.DesktopWindow
import com.sun.jna.platform.WindowUtils
import com.sun.jna.platform.win32.BaseTSD
import com.sun.jna.platform.win32.Kernel32
import com.sun.jna.platform.win32.Kernel32Util
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinUser



public class Main{


    // Exit program if guard fails
    static void guard(boolean failCond, final String msg){
        if(failCond){
            final String nativeError = Kernel32Util.formatMessage(Kernel32.INSTANCE.GetLastError())
            println "${msg}\nNative error: ${Native.getLastError()} - ${nativeError}"
            throw new SystemException(nativeError);
        }
    }

    // TODO: after screwing around, move all this junk out of main
    static void main(String[] args){
        println '=====================================              DSL 5250\n'
        println "${System.getProperty('os.arch')} - ${System.getProperty('os.name')}"
        println "Java - ${System.getProperty('java.version')} @ ${System.getProperty('java.home')}"
        
        int process = Kernel32.INSTANCE.GetCurrentProcessId()
        println "process: ${process}\n"


        final String HOST = 'PUB400.COM'
        final String SESSION = 'A'
        final String PROCESS = 'acslaunch_win-32.exe'

        final int KEYEVENTF_KEYDOWN = 0
        final int KEYEVENTF_KEYUP = 2
        final int VK_CONTROL = 0x11
        final int VK_F5 = 0x74

        
        WinDef.HWND win5250 = User32.INSTANCE.FindWindow(null, "${SESSION} - ${HOST}")
        guard(win5250 == null, 'Error finding 5250 window')

        guard(User32.INSTANCE.SetForegroundWindow(win5250) == 0, 'Error setting foreground window')
        Thread.sleep(500)
        guard(User32.INSTANCE.ShowWindow(win5250, User32.SW_SHOWNORMAL) == 0, 'Error showing window (normal)')
        //guard(User32.INSTANCE.ShowWindow(win5250, User32.SW_SHOWMAXIMIZED) == 0, 'Error showing window (maximize)')
        guard(User32.INSTANCE.UpdateWindow(win5250) == 0, 'Error updating window')
        //guard(User32.INSTANCE.SetFocus(win5250) == null, 'Error focusing window')

        // attachThreadInput 
        // https://java-native-access.github.io/jna/4.2.0/com/sun/jna/platform/win32/User32.html#AttachThreadInput-com.sun.jna.platform.win32.WinDef.DWORD-com.sun.jna.platform.win32.WinDef.DWORD-boolean-

        final WinUser.INPUT input = new WinUser.INPUT()
        input.type = new WinDef.DWORD(WinUser.INPUT.INPUT_KEYBOARD)
        input.input.setType('ki')
        input.input.ki.wScan = new WinDef.WORD(0)
        input.input.ki.time = new WinDef.DWORD(0)
        input.input.ki.dwExtraInfo = new BaseTSD.ULONG_PTR(0) 

        input.input.ki.wVk = new WinDef.WORD(VK_F5)
        input.input.ki.dwFlags = new WinDef.DWORD(KEYEVENTF_KEYDOWN)
        User32.INSTANCE.SendInput(new WinDef.DWORD(1), (WinUser.INPUT[]) input.toArray(1), input.size())

        input.input.ki.wVk = new WinDef.WORD(VK_F5)
        input.input.ki.dwFlags = new WinDef.DWORD(KEYEVENTF_KEYUP)
        User32.INSTANCE.SendInput(new WinDef.DWORD(1), (WinUser.INPUT[]) input.toArray(1), input.size())

    }

}
