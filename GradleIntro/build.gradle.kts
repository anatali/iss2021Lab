/*
This file was added to introduce a multi-project structure
 */
println(" ...... build top-level   ")

/*
SPECIFICATIONS for all the subprojects
 */
allprojects{
    tasks.register("explain"){
        doLast {
            println( "Hello, I'm ${project.name} project. My tasks are:")
            tasks.forEach { println("${it.name}") }
        }
    }
    repeat(4) { counter ->
        tasks.register("commontask$counter") {
            doLast {
                println("I'm commontask number $counter")
            }
        }
    }
}

subprojects {
/*
    // Useful for dependencies
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "java")
    dependencies {
        implementation(project(":app"))
    }
    */
}
