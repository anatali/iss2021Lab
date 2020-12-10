allprojects {
    apply(plugin = "java")
}


subprojects {
//Put instructions for all projects
    /*
    task explain {    //additional configuration to the task hello
        doLast { task ->
            println "Hello, I'm $task.project.name project"
        }
    }*/
    repeat(4) { counter ->
        tasks.register("task$counter") {
            doLast {
                println("I'm task number $counter")
            }
        }
    }
}
