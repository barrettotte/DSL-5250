# Approaches

A dump of notes on the approaches I took just in case I put this project on the shelf, I can somewhat remember why I'm doing things a certain way.

Additionally, I kept all the code for each approach because one day I might want to see how I did something.
Rather than dig through past commits, I can just look in a logical place.



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

This is probably the easiest approach, but let's not "sink" to using 32-bit JVM...there's gotta be a way to do this.



## Approach 2 - JRuby and EHLAPI32.dll
**FAILED**

For some reason I thought I could get a little cheesy and load the DLL with JRuby (one too many beers perhaps).
I started testing with around with vanilla Ruby to see how easy it would be. Loading DLLs was awesome with Ruby.

Go figure; I ran into the same error as before. I don't know why I thought it would be different.



## Approach 3 - JNA and Win32 Programming
**ABANDONED** (read bottom of this section)

Homebrewing up a solution with just straight Win32 in JNA.

Make a utility class for Win32 actions on 5250 emulator.
DSL calls utility functions when evaluating.
Somehow scrape page data, will need it for waiting and possible unit testing?

* [winuser.h](https://docs.microsoft.com/en-us/windows/win32/api/winuser/)
* [Keyboard Input Win32 and C++](https://docs.microsoft.com/en-us/windows/win32/learnwin32/keyboard-input)
* [win32 Virtual Keycodes](https://docs.microsoft.com/en-us/windows/win32/inputdev/virtual-key-codes)
* [JNA User32 Interface](https://java-native-access.github.io/jna/4.2.0/com/sun/jna/platform/win32/User32.html)

Alright cool, now we've got window manipulation and keypresses.
Now the next puzzle, how do we know where the cursor is on screen...
**Side note:** Look into using [java.awt.Robot](https://docs.oracle.com/javase/7/docs/api/java/awt/Robot.html) for keypresses instead (might be easier).


Emulator coordinates are always in view... just not accessible
* Not available with a CTRL+A and CTRL+C
* Not consistent, depends on resolution :/


**Brainstorm:**
* Idea A
  * open window fixed resolution
  * **java.awt.Robot.createScreenCapture()** for bottom right corner of screen (10-20% of resolution) 
  * use OCR or other image processing to get coordinates
  * Could be used for an additional test option - screenshots with each step
  * EXPENSIVE - I'm assuming...maybe there are lightweight solutions.
    * could be kind of cheap -> dealing with 1-bit color (black or white)
* Idea B
  * open window fixed resolution
  * use **Color java.awt.Robot.getPixelColor(int x, int y)**
  * Grab a block of pixels, parse coordinates (only dealing with 6 characters - numbers and a slash...doable)
  * "Might" be lighter than loading Image buffer and loading it into an image processing library
  * Fonts destroy this solution completely...enforce font? (yikes)
* Idea C
  * open window fixed resolution
  * convert local mouse position to 5250 position
  * figure out position from local mouse position
  * re-position with clicking

**hmmm idk about this one...**


There are a ton of flaws with this approach and the ideas I brainstormed:
* Windows only...yes that's a huge one
* Where am I? Getting screen position is really not going to be practical
* Setting position will be just as bad
  * Click mouse upper left hand corner, position with arrow keys...?
* Gather string at position...same issue
* Wait for string to appear...same issue
* ...you get the point

As much as I really wanted to get this working, its time I abandon this approach and go for a better one.


## Approach 4 - Switch to TN5250J
I really wanted to just use ACS 5250, but its just not doable with the approaches I've tried.
My coworker made [TerminalDriver](https://github.com/terminaldriver/terminaldriver), based around TN5250J.
In this repository he clearly demonstrates being able to programatically fetch 5250 screen contents
AND maintain knowledge of current cursor position.


## Other Possible Approaches
* Web enabled 5250 terminal and use Selenium
  * web navigator > 5250 emulator
  * TN5250j
* DSL generates HOD macros - https://www.ibm.com/support/knowledgecenter/SSS9FA_11.0.0/com.ibm.hod.doc/doc/macro/macro.html?cp=SSS9FA_11.0.0/7
* DSL generates AutoHotKey scripts
* Recompiling 32-bit dll to 64-bit
  * No clue where I'd start...probably not feasable
* VBA?
  * Tons and tons of examples of doing it on earlier 5250 emulators 
  * I dont think ACS supports it - https://www.tetcommunity.com/blogs/building-reports-in-excel-using-ibm-pcomm-automation-api


## Link Dump
* Automating data transfer ACS - https://www.ibm.com/support/pages/automating-acs-data-transfer
* Host on Demand https://www.ibm.com/us-en/marketplace/host-on-demand
* Jagacy - https://dzone.com/articles/automated-acceptance-testing-for-mainframe-with-cu
  * Paid...nah
* [COM4J](https://github.com/kohsuke/com4j)
* https://www.ibm.com/support/pages/node/720725
* C:\Users\Public\IBM\ClientSolutions
* https://github.com/phelgren/web5250
* https://github.com/java-native-access/jna/blob/master/contrib/platform/test/com/sun/jna/platform/win32/User32Test.java#L208





Recording screen per step with **java.awt.Toolkit** and keypresses
* open window in forced resolution
* simulate ```CTRL+A``` and ```CTRL+C```
* ``` println (Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor) as String)```
