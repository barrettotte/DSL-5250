# DSL-5250

A dummy's approach to making a DSL to automate a 5250 emulator.


## Purpose
This project was used to reinforce my limited knowledge of Groovy and learn how to make a Domain Specific Language (DSL).
Additionally, 5250 emulator automation seemed like a fascinating topic to screw around with.

My coworker ([Ryan Eberly](https://github.com/ryaneberly)) sparked the motivation for this project, he has an awesome
testing framework built around TN5250J in [Terminal Driver](https://github.com/terminaldriver/terminaldriver).

I think I could have taken this project a lot further, but for now I am satisfied with it; Time to move onto the next "shiny".


## DSL Features
* Get and set cursor position
* Get and set screen contents
* Wait for screen content to appear
* Press command keys F1-F24
* Press ENTER, TAB, ESCAPE
* Record screen text to text file
* Capture screenshot


## Setup
* Download TN5250J jar and place in **lib/** - ```./getTN5250J.sh```


## Syntax
TODO:


## Approach
I took a few other approaches before the current implementation, skim through them in [docs/approaches.md](docs/approaches.md)


## TODO
* ```log.info``` instead of ```println``` - SLF4J? http://docs.groovy-lang.org/2.4.2/html/gapi/groovy/util/logging/Slf4j.html
* Reorganize Main.groovy into utilities and whatnot
* Basic Java swing application for entering credentials and whatnot? 


## References
* [Groovy Domain Specific Languages](http://docs.groovy-lang.org/docs/latest/html/documentation/core-domain-specific-languages.html)
* [Groovy DSL Builders](https://medium.com/@musketyr/groovy-dsl-builders-1-the-concept-2d5a97fa0a51)
* [IBM Emulator Programming](https://www.ibm.com/support/knowledgecenter/SSEQ5Y_5.9.0/com.ibm.pcomm.doc/books/html/emulator_programming08.htm)
* [IBM Introduction to Emulator APIs](https://www.ibm.com/support/knowledgecenter/SSEQ5Y_6.0.0/com.ibm.pcomm.doc/books/html/emulator_programming06.htm)
* [JNA API Documentation](https://java-native-access.github.io/jna/4.2.1/overview-summary.html)
