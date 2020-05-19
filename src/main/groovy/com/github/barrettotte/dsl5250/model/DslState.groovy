package com.github.barrettotte.dsl5250.model

import groovy.transform.CompileStatic

@CompileStatic
class DslState{

    String stageName
    Integer stageIndex
    Integer stagesLength

    Integer stepIndex
    Integer stepsLength

    Boolean connected

}