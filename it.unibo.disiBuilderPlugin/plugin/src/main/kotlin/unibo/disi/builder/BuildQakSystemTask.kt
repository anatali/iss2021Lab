package unibo.disi.builder

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.*
import org.gradle.api.tasks.options.Option
import javax.inject.Inject


//See https://proandroiddev.com/stop-using-gradle-buildsrc-use-composite-builds-instead-3c38ac7a2ab3
// Inject annotation is required!
open class BuildQakSystemTask @Inject constructor() : DefaultTask() {
    @Input var prefix         ="BuildQakSystemTask | "
    @Input var projectDir     = System.getProperty("user.dir")
    @Input var sysName        ="-"
    //private var qakProjectDir = projectDir
    //private var qakSysName    = sysName

    @Option(option = "projectdir", description = "Project directory.")
    fun setQakProjectDir(dir : String) {  projectDir = dir }

    @Option(option = "sysname", description = "Project directory.")
    fun setQakSysName(systemName  : String) {  sysName=systemName }

    @TaskAction
    fun build() {
        println("$prefix input projectDir=$projectDir  sysName=$sysName")
        if( projectDir != "-" && sysName != "-" ) GeneratorQak.genQak(projectDir, sysName)


    }
}

