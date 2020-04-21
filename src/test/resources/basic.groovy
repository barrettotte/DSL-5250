{
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