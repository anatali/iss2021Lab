package unibo.disi.builder

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.*
import org.gradle.api.tasks.options.Option
import javax.inject.Inject

// Inject annotation is required!
open class BuildActorTask @Inject constructor() : DefaultTask() {
    @Input var prefix        = "BuildActorTask | "
    @Input var actorModel    = "-"

    @Option(option = "model", description = "Model to be converted in code.")
    fun setModel(mymodel : String) { actorModel = mymodel }

    @TaskAction
    fun build() {
        println("$prefix input actorModel=$actorModel   ")
        if( actorModel != "-") Generator.genCodeFromModel("$actorModel")
        else  println("sorry, $actorModel does not exist")
    }
}

