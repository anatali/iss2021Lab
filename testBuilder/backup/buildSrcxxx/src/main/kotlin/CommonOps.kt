import org.gradle.api.DefaultTask
import org.gradle.api.tasks.*
import javax.inject.Inject
//import disi.builder.*

//See https://proandroiddev.com/stop-using-gradle-buildsrc-use-composite-builds-instead-3c38ac7a2ab3
// Inject annotation is required!
open class CommonTask @Inject constructor() : DefaultTask() {
    @Input var prefix        ="buildSrc/CommonTask | "
    @Input var actorModel    ="todo"
    @TaskAction
    fun commonOp() {
        println("$prefix $actorModel  ")
        //generator.genCodeFromModel("$actorModel")
    }
}

