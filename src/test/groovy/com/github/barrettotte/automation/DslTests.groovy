package com.github.barrettotte.automation

import static org.junit.jupiter.api.Assertions.assertEquals

import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test


class DslTests{

	@Test
	void test_basic() {
		Dsl.build{
			environment{
				SYSTEM = "PUB400.COM"
				USERNAME = "OTTEB"
				PASSWORD = "PASSWORD"
			}
			stages{
				stage("LOGIN"){
					steps{env->
						position 6,53
						send "${env.USERNAME}"
						position 7,53
						send "${env.PASSWORD}",true
					}
				}
				stage("TEST"){
					steps{
						position 20,7
						send "DSPLIBL"
						cmd 12
					}
				}
				stage("LOGOFF"){
					steps{
						position 20,7
						send "SIGNOFF"
					}
				}
			}
		}
	}

	@Test
	void test_read(){
		// TODO:
		assertEquals(1,1)
	}

}
