import org.gradle.api.DefaultTask
import org.gradle.api.tasks.*
import javax.inject.Inject
//import disi.builder.*
import alice.tuprolog.*

// Inject annotation is required!
open class DisGenTask @Inject constructor() : DefaultTask() {
    @Input var actorModel ="todo"
    @TaskAction
    fun genOp() {
         println("DisGenTask $actorModel  ")
         //disi.builder.generator.genCodeFromModel("$actorModel")
    }
}