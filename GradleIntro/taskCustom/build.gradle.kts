import org.gradle.internal.jvm.Jvm
println(" ...... build in taskCustom sub-project  ")
/*
tasks.register("hello") {
    doLast {
        println("Hello world from taskCustom -- this=${this}  ")
        //tasks.forEach { println("${it.name}") }
    }
}
*/
tasks.register<Copy>("mycopy") {    //Registers a new task of type Copy and configures it
    println("projectDir= $projectDir") //GradleIntro\taskCustom
    println("buildDir  = $buildDir")   //GradleIntro\taskCustom\build
    from("$projectDir/../app/src"){
        exclude( "**/main/resources", "**/test" )
    }
    into( "../copiedFiles" )
}

task<Exec>("hello") {
    workingDir("$projectDir")
    if (System.getProperty("os.name").toLowerCase().contains("windows")) {
        commandLine(
                "cmd", "/c",
                "echo", "Hello xxx from Windows"    )
    } else {
        commandLine(
                "sh", "-c",
                "echo", "Hello xxx from NO-Windows" )
    }
}

/*
project.exec {
    commandLine( "$projectDir/cmd2.bat" )
}
*/
tasks.register<Exec>("mycmdline") { //Inline function with reified type!
// Configuration action is of type T.() -> Unit, in this case Exec.T() -> Unit
    val javaExecutable = Jvm.current().javaExecutable.absolutePath
    val command        = "cmd1.bat"
     //this is a method of class org.gradle.api.Exec
     commandLine("$projectDir/cmd1.bat" )
     commandLine( javaExecutable, "-version" )
// There is no need of doLast / doFirst, actions are already configured
// Still, we may want to do something before or after the task has been executed
    doLast {  println("-------- exec END") }
    doFirst { println("-------- exec STARTS" ) }
}

