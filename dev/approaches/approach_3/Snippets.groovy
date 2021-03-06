// discarded snippets from screwing around with JNA
// Maybe I'll need one of these some day...who knows


// loop over windows
for(final DesktopWindow dw : WindowUtils.getAllWindows(true)){
    final String title = dw.getTitle().trim()
    final String fpath = dw.getFilePath().trim()

    if(title.length() > 0 && title == "${SESSION} - ${HOST}" && fpath.endsWith(PROCESS)){
        println "${title} -> ${fpath}"
        WinDef.HWND hwnd = dw.getHWND()
    }
}



// Find window in a different way (with a callback)
final User32Dll user32 = User32Dll.INSTANCE
boolean notFound = user32.EnumWindows(new WinUser.WNDENUMPROC(){
    @Override
    public boolean callback(WinDef.HWND hWnd, Pointer arg1){
        final char[] windowText = new byte[512]
        user32.GetWindowTextW(hWnd, windowText, 512)

        final String title = Native.toString(windowText)
        if (title.length() > 0 && title == "${SESSION} - ${HOST}"){
            println 'Got it'
            user32.ShowWindow(hWnd, User32.SW_RESTORE)
            return false
        }
        return true
    }
}, null)



// Trying to figure out how to set foreground window
// Snippet usesful for showing how to setup User32.dll interface
public interface User32Dll extends StdCallLibrary {
    final User32Dll INSTANCE = Native.loadLibrary('user32', User32Dll.class) as User32Dll

    boolean EnumWindows(WinUser.WNDENUMPROC lpEnumFunc, Pointer data)
    WinDef.HWND SetFocus(WinDef.HWND hWnd)
    int GetWindowTextW(WinDef.HWND hWnd, char[] lpString, int nMaxCount)
    boolean ShowWindow(WinDef.HWND hWnd, int nCmdShow)
        
    boolean AllowSetForegroundWindow(WinDef.DWORD dwProcessId) // https://docs.microsoft.com/en-us/windows/win32/api/winuser/nf-winuser-allowsetforegroundwindow
}

WinDef.DWORD p = new WinDef.DWORD(Kernel32.INSTANCE.GetCurrentProcessId())
guard(User32Dll.INSTANCE.AllowSetForegroundWindow(p) != 0, 'Error AllowSetForegroundWindow')
// throws error 0 - access is denied



// Useful debug info
println "${System.getProperty('os.arch')} - ${System.getProperty('os.name')}"
println "Java - ${System.getProperty('java.version')} @ ${System.getProperty('java.home')}"     
int process = Kernel32.INSTANCE.GetCurrentProcessId()
println "process: ${process}\n"



