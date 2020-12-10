import org.gradle.internal.jvm.Jvm
println(" ...... build in taskCustom sub-project  ")

tasks.register("hello") {
    doLast {
        println("Hello world from taskCustom -- this=${this}  ")
        //tasks.forEach { println("${it.name}") }
    }
}


tasks.register<Exec>("printJavaVersion") { //Inline function with reified type!
// Configuration action is of type T.() -> Unit, in this case Exec.T() -> Unit
    val javaExecutable = Jvm.current().javaExecutable.absolutePath
    commandLine( //this is a method of class org.gradle.api.Exec
            javaExecutable, "-version"
    )
// There is no need of doLast / doFirst, actions are already configured
// Still, we may want to do something before or after the task has been executed
    doLast {  println("-------- invocation complete") }
    doFirst { println("-------- Ready to invoke $javaExecutable") }
}

