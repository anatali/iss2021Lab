package unibo.disi.builder

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.*
import org.gradle.api.tasks.options.Option
import javax.inject.Inject
//import disi.builder.*

//See https://proandroiddev.com/stop-using-gradle-buildsrc-use-composite-builds-instead-3c38ac7a2ab3
// Inject annotation is required!
open class BuildQakSystemTask @Inject constructor() : DefaultTask() {
    @Input var prefix         ="BuildQakSystemTask | "
    @Input var projectDir     ="-"
    @Input var sysName        ="-"
    private var qakProjectDir = "-"
    private var qakSysName    = "-"

    @Option(option = "projectdir", description = "Project directory.")
    fun setQakProjectDir(dir : String) { qakProjectDir = dir; projectDir = dir }

    @Option(option = "sysname", description = "Project directory.")
    fun setQakSysName(systemName  : String) { qakSysName = systemName; sysName=systemName }

    @TaskAction
    fun build() {
        println("$prefix input qakProjectDir=$qakProjectDir  qakSysName=$qakSysName")
        if( projectDir != "-" && sysName != "-" ) GeneratorQak.genQak(projectDir, sysName)

    }
}

