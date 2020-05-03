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



## Research
* Host on Demand https://www.ibm.com/us-en/marketplace/host-on-demand
* Jagacy - https://dzone.com/articles/automated-acceptance-testing-for-mainframe-with-cu
* https://www.ibm.com/support/pages/node/720725



## Possible Approaches
* Open 32-bit DLL with JRuby
* Homebrew up something - reading windows using JNA https://stackoverflow.com/questions/54353050/can-i-read-text-from-another-window-using-java
* [COM4J](https://github.com/kohsuke/com4j)
* DSL generates HOD macros - https://www.ibm.com/support/knowledgecenter/SSS9FA_11.0.0/com.ibm.hod.doc/doc/macro/macro.html?cp=SSS9FA_11.0.0/7
* DSL generates AutoHotKey scripts
* Recompiling 32-bit dll to 64-bit?
* VBA? I dont think ACS supports it - https://www.tetcommunity.com/blogs/building-reports-in-excel-using-ibm-pcomm-automation-api

