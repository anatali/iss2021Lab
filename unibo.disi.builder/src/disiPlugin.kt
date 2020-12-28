import org.gradle.api.Plugin
import org.gradle.api.Project

class disiPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.task("hellodisi") {
            it.doLast {
                println("Hello from the disiPlugin")
            }
        }
    }
}

