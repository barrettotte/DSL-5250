# DSL-5250

A dummy's approach to making a simple DSL for headless interaction with a 5250 emulator.


## Purpose
This project was used to reinforce my limited knowledge of Groovy and learn how to make a Domain Specific Language (DSL).
Additionally, 5250 emulator automation seemed like a fascinating topic to screw around with.

Possible applications could be basic automated tasks/testing and general screen scraping.

My coworker ([Ryan Eberly](https://github.com/ryaneberly)) sparked the motivation for this project, he has an awesome 5250
testing project built around TN5250J called [Terminal Driver](https://github.com/terminaldriver/terminaldriver).

I think I could have taken this project a lot further (this is a super shallow implementation), but for now I am satisfied with it; Time to move onto the next "shiny".


## DSL Features
* Get and set cursor position
* Get and set screen contents at specified position or at cursor
* Check screen position for string
* Record screen contents to text file
* Press command keys F1-F24 and other keys ENTER, TAB, ESCAPE, PAGEUP, PAGEDOWN, etc.


## Setup
* Download TN5250J jar and place in **libs/**


## Syntax
TODO:


## Approach
I took a few other approaches before the current implementation, skim through them in [dev/approaches](dev/approaches). Here is the outline:
1. JNA and EHLAPI32.dll - **failed**
2. JRuby and EHLAPI32.dll - **failed**
3. JNA and Win32 Programming - **barely worked...abandoned**
4. TN5250J - **current**


## Possible Enhancements
* Set and get field by name
* Smarter field traversing - next,prev,first,last
* HOD macro generation
* Image screenshots


## Commands
* Interactive run (entering masked password) - ```gradlew run --no-daemon```


## References
* [Groovy Domain Specific Languages](http://docs.groovy-lang.org/docs/latest/html/documentation/core-domain-specific-languages.html)
* [Groovy DSL Builders](https://medium.com/@musketyr/groovy-dsl-builders-1-the-concept-2d5a97fa0a51)
* [IBM Emulator Programming](https://www.ibm.com/support/knowledgecenter/SSEQ5Y_5.9.0/com.ibm.pcomm.doc/books/html/emulator_programming08.htm)
* [IBM Introduction to Emulator APIs](https://www.ibm.com/support/knowledgecenter/SSEQ5Y_6.0.0/com.ibm.pcomm.doc/books/html/emulator_programming06.htm)
* [JNA API Documentation](https://java-native-access.github.io/jna/4.2.1/overview-summary.html)
