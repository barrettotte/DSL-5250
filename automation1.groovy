import static com.github.barrettotte.dsl.Dsl.automation


automation{

  environment{
    SYSTEM = "DEV400"
    USER = "OTTEB"
  }

  stages{

    stage("LOGIN"){
      steps{
        position 1,2
        send "$USER"
        position 3,2
        send "PASSWORD"
        send "\n"
      }
    }

    stage("TEST"){
      steps{
        position 1,2
        send "FOO"
        cmd 14
      }
    }

  }

}