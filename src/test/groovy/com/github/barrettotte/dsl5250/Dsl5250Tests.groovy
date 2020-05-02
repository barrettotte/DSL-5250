package com.github.barrettotte.dsl5250

import com.github.barrettotte.dsl5250.utils.Dsl5250Utils

import org.junit.jupiter.api.Test

class Dsl5250Tests{

	private final Dsl5250 dsl = new Dsl5250()

	@Test
	void test_eval_manual(){
		dsl.eval{
			environment{
				SYSTEM = 'PUB400.COM'
				USERNAME = 'OTTEB'
				PASSWORD = 'PASSWORD'
			}
			stages{
				stage('LOGIN'){
					steps{env->
						position 6,53
						send "${env.USERNAME}"
						position 7,53
						send "${env.PASSWORD}",true
						waitms 1000
					}
				}
				stage('TEST'){
					steps{
						position 20,7
						send 'DSPLIBL'
						cmd 12
					}
				}
				stage('LOGOFF'){
					steps{
						position 20,7
						send 'SIGNOFF'
					}
				}
			}
		}
	}

	@Test
	void test_eval_file(){
		final f = Dsl5250Utils.closureFromFile(this.getClass().getResource('/basic.groovy'))
		dsl.eval(f)
	}

}
