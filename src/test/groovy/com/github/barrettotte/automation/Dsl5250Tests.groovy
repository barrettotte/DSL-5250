package com.github.barrettotte.automation

import com.github.barrettotte.automation.dsl.Dsl5250

import static org.junit.jupiter.api.Assertions.assertEquals

import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test


class Dsl5250Tests{

	@Test
	void test_eval_manual() {
		Dsl5250.eval{
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
	void test_eval_closure(){
		final def dsl = closureFromFile('/basic.groovy')
		assert dsl instanceof Closure
		Dsl5250.eval(dsl)
	}

	@Test
	void test_eval_file(){
		final File f = new File(this.getClass().getResource('/basic.groovy').toURI())
		Dsl5250.eval(f)
	}


	/****** Utils *******/
	Closure closureFromFile(final String filename){
		return new GroovyShell().evaluate('return ' + this.getClass().getResource(filename).text)
	}

}
