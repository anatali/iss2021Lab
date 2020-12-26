import org.gradle.api.DefaultTask
import org.gradle.api.tasks.*
import javax.inject.Inject
//import disi.builder.*

// Inject annotation is required!
open class DisGenTask @Inject constructor() : DefaultTask() {
    @TaskAction
    fun genOp() {
         generator.genCodeFromModel("demo0")
    }
}