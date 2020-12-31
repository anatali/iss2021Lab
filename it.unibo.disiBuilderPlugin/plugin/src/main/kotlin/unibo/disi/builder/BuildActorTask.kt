package unibo.disi.builder

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.*
import javax.inject.Inject
//import disi.builder.*

//See https://proandroiddev.com/stop-using-gradle-buildsrc-use-composite-builds-instead-3c38ac7a2ab3
// Inject annotation is required!
open class BuildActorTask @Inject constructor() : DefaultTask() {
    @Input var prefix        ="BuildActorTask | "
    @Input var actorModel    ="todo"
    @TaskAction
    fun build() {
        println("$prefix $actorModel  ")
        Generator.genCodeFromModel("$actorModel")
    }
}

