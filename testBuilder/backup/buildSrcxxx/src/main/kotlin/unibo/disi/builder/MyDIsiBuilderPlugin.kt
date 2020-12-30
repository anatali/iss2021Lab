package unibo.disi.builder

import org.gradle.api.Plugin
import org.gradle.api.Project

class MyDisiBuilderPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.task("buildDisi") {
            doLast {

                val s = generator.genFilePathName("pippo")
                 println("Hello from MyDisiBuilderPlugin plugin $s ")

                //generator.genCodeFromModel("demo0")
            }
        }
    }
}