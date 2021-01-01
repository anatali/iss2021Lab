package unibo.disi.builder

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.*
import org.gradle.api.tasks.options.Option
import javax.inject.Inject
//import disi.builder.*

//See https://proandroiddev.com/stop-using-gradle-buildsrc-use-composite-builds-instead-3c38ac7a2ab3
// Inject annotation is required!
open class BuildActorTask @Inject constructor() : DefaultTask() {
    @Input var prefix        ="BuildActorTask | "
    @Input var actorModel    ="-"
    private var model = "-"

    @Option(option = "model", description = "Model to be converted in code.")
    fun setModel(mymodel : String) { this.model = mymodel }

    @TaskAction
    fun build() {
        println("$prefix input actorModel=$actorModel argmodel=$model ")
        if( actorModel != "-") Generator.genCodeFromModel("$actorModel")
        if( model != "-") Generator.genCodeFromModel("$model")
    }
}

