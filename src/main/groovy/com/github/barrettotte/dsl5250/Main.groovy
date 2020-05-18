package com.github.barrettotte.dsl5250

import com.github.barrettotte.dsl5250.constant.Key

import groovy.json.JsonSlurper

class Main{

    static void main(String[] args){
        final config = new JsonSlurper().parse(new File(this.getClass().getResource('/config.json').toURI()))
        final List<String> qrpgleMembers = []

        Dsl5250.eval{
            environment{
                outputPath = 'out'
                host = config['host']
                user = config['user']
                password = config['password']
            }
            stages{
                stage('LOGIN'){
                    steps{env ->
                        position 6,53
                        send env.user
                        capture()
                        position 7,53
                        send env.password,true  // true -> masks text in log
                        press Key.ENTER
                        waitms 2500
                        if(check('Display Program Messages',1,28) || check('Display Messages',1,33)){
                            press Key.ENTER
                            waitms 1000
                        }
                        capture()
                        waitms 1000
                    }
                }
                stage('DSPLIBL'){
                    steps{
                        send 20,7,'DSPLIBL'
                        press Key.ENTER
                        waitms 1000
                        capture()        
                        cmd 12
                        waitms 1000
                    }
                }
                stage('EXTRACT'){
                    steps{
                        def system = extract(2,72,10) // testing extract method  (row, col, length)
                        println "system - $system"
                        position 20,7
                        send 'WRKMBRPDM BOLIB/QRPGLESRC'
                        press Key.ENTER
                        waitms 1000

                        while(!(check('You have reached the bottom of the list.',24,2))){
                            capture()
                            qrpgleMembers.addAll(extract(11,7,10,8).collect{i-> i.replaceAll('\\s','')}.findAll{i-> i.length() > 0})
                            press Key.PG_DOWN
                            waitms 1000
                        }
                        cmd 3
                        waitms 1000
                    }
                }
                stage('LOGOFF'){
                    steps{
                        position 20,7
                        send 'SIGNOFF'
                        press Key.ENTER
                        waitms 1000
                        capture()
                    }
                }
            }
        }
        println 'BOLIB/QRPGLESRC\n' + qrpgleMembers
    }

}