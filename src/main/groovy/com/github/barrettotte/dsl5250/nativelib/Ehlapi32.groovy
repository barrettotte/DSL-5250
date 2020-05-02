package com.github.barrettotte.dsl5250.nativelib

import com.sun.jna.Library
import com.sun.jna.Native


interface Ehlapi32 extends Library{
    
    Ehlapi32 INSTANCE = (Ehlapi32) Native.loadLibrary('EHLAPI32', Ehlapi32.class);

    int wd_ConnectPS(int hInstance, String shortName);
    int wd_SendKey(int hInstance, String keyData);
    
}
