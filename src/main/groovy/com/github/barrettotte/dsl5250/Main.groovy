package com.github.barrettotte.dsl5250

import com.github.barrettotte.dsl5250.Dsl5250
import com.github.barrettotte.dsl5250.constant.Key

import groovy.json.JsonSlurper

public class Main{

    static void main(String[] args){ 
        final Dsl5250 dsl = new Dsl5250()
        final config = new JsonSlurper().parse(new File(this.getClass().getResource('/config.json').toURI()))

        String firstLib

        dsl.eval{
            environment{
                outputPath = 'out'
                host = config['host']
                user = config['user']
                password = config['password']
            }
            stages{
                stage('LOGIN'){
                    steps{env->
                        position 6,53
                        send env.user
                        capture()
                        position 7,53
                        send env.password,true  // true -> mask in log
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
                // stage('DSPLIBL'){
                //     steps{
                //         position 20,7
                //         send 'DSPLIBL'
                //         press Key.ENTER
                //         waitms 1000
                //         capture()        
                //         cmd 12
                //         waitms 1000
                //     }
                // }
                // stage('WRKMBRPDM'){
                //     steps{
                //         position 20,7
                //         send 'WRKMBRPDM BOLIB/QRPGLESRC'
                //         press Key.ENTER
                //         waitms 1000
                //         capture()
                //         // while(!check('Bottom',19,73)) liblist << (subfile contents)
                //     }
                // }
                // stage('LOGOFF'){
                //     steps{
                //         position 20,7
                //         send 'SIGNOFF'
                //         press Key.ENTER
                //         waitms 1000
                //         capture()
                //     }
                // }
            }
        }

        //println 'first library -> ' + firstLib
    }
}
