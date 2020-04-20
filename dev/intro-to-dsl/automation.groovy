import static com.github.barrettotte.dsl.Dsl.automation

// https://www.youtube.com/watch?v=i9pNYW1Pg9A

automation{

  environment{
    SYSTEM = "PUB400.COM"
    USER = "OTTEB"
  }

  stages{
    stage("LOGIN"){
      steps{ Map env->
        position 6,53
        send "${env.USER}"
        position 7,53
        send "PASSWORD"
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