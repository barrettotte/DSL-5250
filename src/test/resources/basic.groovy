{
  environment{
    host = 'PUB400.COM'
    user = 'OTTEB'
    password = 'PASSWORD'
  }
  stages{
    stage('LOGIN'){
      steps{env->
        position 6,53
        send "${env.user}"
        position 7,53
        send "${env.password}",true
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