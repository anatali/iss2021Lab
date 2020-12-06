/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Java application project to get you started.
 * For more details take a look at the 'Building Java & JVM projects' chapter in the Gradle
 * User Manual available at https://docs.gradle.org/6.7.1/userguide/building_java_projects.html
 */

plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    application
}

repositories {
    // Use JCenter for resolving dependencies.
    jcenter()
}

dependencies {
    // Use JUnit test framework.
    testImplementation("junit:junit:4.13")

    // This dependency is used by the application.
    implementation("com.google.guava:guava:29.0-jre")
}

application {
    // Define the main class for the application.
    mainClass.set("demo.App")
}


/*
ADDED TASKS
 */

tasks.register("notgood"){
    println("Message shown during the configuration phase: no task action defined ")
}

tasks.register("welcome") { //can be declared before hello and count
    dependsOn("hello")
    dependsOn("count")
    doLast {
        println("You are welcome ")
    }
}

tasks.register("hello") {
    doLast {
        println("Hello world from added Task!")
    }
}

tasks.register("count") {
    doLast {
        repeat(4) { print("$it ") }
        println()
    }
}

repeat(4) { counter ->
    tasks.register("task$counter") {
        doLast {
            println("I'm task number $counter")
        }
    }
}

tasks.named("task0") { dependsOn("task3", "task2") }

val t0 = tasks.named("task0")
t0{
    doFirst {
        println("Configured later, but executed as first in task named ${t0.name}")
    }
}
t0{
    doLast {
        println("Another last of task named ${t0.name}")
    }
}

tasks.named("task2") { mustRunAfter(tasks.named("task3")) }
/*
tasks.getByName("task3") { // let's find an existing task
    doFirst { // Similar to doLast, but adds operations in head
        println("Task3: code configured later, but executed as first.")
    }
}

 */

//DOES NOT WORK
//defaultTasks("hello", "myclean")

task("myclean") {
    description = "A task to clean."
    doLast {
        println("My Default Cleaning!")
    }
}
/* */

open class CustomTask @javax.inject.Inject constructor(
        private val message: String,
        private val number: Int
) : DefaultTask()
tasks.register<CustomTask>("myTask", "hello", 42)


open class GreetingTask : DefaultTask() {
    @TaskAction
    fun greet() {
        println("hello from GreetingTask")
    }
}
// Create a task using the task type
tasks.register<GreetingTask>("greetings")


//import org.gradle.internal.jvm.Jvm // Jvm is part of the Gradle API
tasks.register<Exec>("printJavaVersion") { // Do you Recognize this? inline function with reified type!
// Configuration action is of type T.() -> Unit, in this case Exec.T() -> Unit
            val javaExecutable = org.gradle.internal.jvm.Jvm.current().javaExecutable.absolutePath
            commandLine( // this is a method of class org.gradle.api.Exec
                    javaExecutable, "-version"
            )
// There is no need of doLast / doFirst, actions are already configured
// Still, we may want to do something before or after the task has been executed
            doLast { println("$javaExecutable invocation complete") }
            doFirst { println("Ready to invoke $javaExecutable") }
        }
