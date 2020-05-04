# Approaches

Notes on the approaches I took just in case I need to backtrack


## Approach 1 - JNA and EHLAPI32.dll
**FAILED**

* IBM Access Client Solutions EHLAPI (additional download) -> add to PATH ```C:\Program Files (x86)\IBM\EHLLAPI```
* [Java Native Access (JNA)](https://github.com/java-native-access/jna) - use DLLs in Java
* [Dependency Walker](http://www.dependencywalker.com/) fails to process EHLAPI32.dll ... ```>:(```
* https://www.tetcommunity.com/blogs/emulator-high-level-language-api-ehllapi-in-ibm-personal-communications


Make Groovy class for interacting with EHLAPI32.dll 
* Example - ```Ehlapi32 ehlapi32 = Native.loadLibrary('ehlapi32', Ehlapi32.class) as Ehlapi32```
* No matter what I tried... - ```java.lang.UnsatisfiedLinkError: %1 is not a valid Win32 application```


I **think** none of this will work because I'm trying to use a 32-bit DLL in 64-bit JVM
  * ```C:\Program Files (x86)\IBM\EHLLAPI\readme.html``` - "Supported environments include Windows operating systems with Access Client Solutions running in a 32-bit Java virtual machine."


## Approach 2 - JRuby and EHLAPI32.dll
**FAILED**

For some reason I thought I could get a little cheesy and load the DLL with JRuby. 
I started tested with Ruby to see how easy it would be. Loading DLLs was awesome with Ruby.

I ran into the same error as before, I don't know why I thought it would be different.



## Approach 3 - JNA and Win32 Programming
Homebrewing up a solution with just straight Win32 in JNA.

Make a utility class for Win32 actions on 5250 emulator.
DSL calls utility functions when evaluating.
Somehow scrape page data, will need it for waiting and possible unit testing?

* [winuser.h](https://docs.microsoft.com/en-us/windows/win32/api/winuser/)
* [Keyboard Input Win32 and C++](https://docs.microsoft.com/en-us/windows/win32/learnwin32/keyboard-input)
* [win32 Virtual Keycodes](https://docs.microsoft.com/en-us/windows/win32/inputdev/virtual-key-codes)
* [JNA User32 Interface](https://java-native-access.github.io/jna/4.2.0/com/sun/jna/platform/win32/User32.html)


## Research
* Host on Demand https://www.ibm.com/us-en/marketplace/host-on-demand
* Jagacy - https://dzone.com/articles/automated-acceptance-testing-for-mainframe-with-cu
  * Paid...nah
* [COM4J](https://github.com/kohsuke/com4j)
* https://www.ibm.com/support/pages/node/720725


## Possible Approaches
* DSL generates HOD macros - https://www.ibm.com/support/knowledgecenter/SSS9FA_11.0.0/com.ibm.hod.doc/doc/macro/macro.html?cp=SSS9FA_11.0.0/7
* DSL generates AutoHotKey scripts
* Recompiling 32-bit dll to 64-bit
  * No clue where I'd start...probably not feasable
* VBA?
  * Tons and tons of examples of doing it on earlier 5250 emulators 
  * I dont think ACS supports it - https://www.tetcommunity.com/blogs/building-reports-in-excel-using-ibm-pcomm-automation-api

