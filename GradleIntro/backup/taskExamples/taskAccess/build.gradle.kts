//Dynamically created tasks
/*
repeat(4) { counter ->
    tasks.register("task$counter") {
        doLast {
            println("I'm task number $counter")
        }
    }
}
*/
tasks.register("hello") {
    doLast {
        println("Hello world from taskAccess - this=${this}  ")
        //tasks.forEach { println("${it.name}") }
    }
}

//Adding a dependency on a task
tasks.named("task0") { 	//Accessing a task via API -
    dependsOn("task3", "task2")
}

//Control order execution
tasks.named("task2") { mustRunAfter(tasks.named("task3")) }

//Modify an existing behaviour
val t2 = tasks.named("task2")

t2{	//Accessing a task via API - adding behaviour
    doFirst {
        println("Configured later, but executed as first in task named ${t2.name}")
    }
}
t2{ //Accessing a task via API - adding behaviour
    doLast {
        println("Another last of task named ${t2.name}")
    }
}

/*
..\gradlew -q :taskAccess:task3

gradlew -q :taskAccess:task2

gradlew -q :taskAccess:task0

 */


