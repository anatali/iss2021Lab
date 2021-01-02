package unibo.disi.builder

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option
import javax.inject.Inject

open class ProjectInfoTask @Inject constructor()  : DefaultTask() {
     enum class Format {
        PLAIN, JSON
    }
    private var format = Format.PLAIN

    init {
        println("ProjectInfoTask CREATED with input option format=$format")
    }

    @Option(option = "format", description = "Output format of the project information.")
    fun setFormat(myformat: Format) { this.format = myformat }


    @TaskAction
    fun projectInfo() {
        //println("format=$format")

        when (format) {
            Format.PLAIN ->
                println( "projectName= ${getProject().getName()} : ${getProject().getVersion()}")
            Format.JSON ->
                println("{\n" +
                        "    \"projectName\": \"" + getProject().getName() + "\"\n" +
                        "    \"version\": \"" + getProject().getVersion() + "\"\n}")
            //else -> throw IllegalArgumentException("Unsupported format: " + format);
        }

    }


}