//Dynamically created tasks
println(" ...... build in taskAccess sub-project  ")

tasks.named("explain") {
    doFirst{	//additional configuration to the task explain
        println( "	- I'm the taskAccess sub-project: START - ")
    }
    doLast{	//additional configuration to the task explain
        println( "	- I'm the taskAccess sub-project: END - ")
    }
}

tasks.register("hello") {
    doLast {
        println("Hello world from taskAccess -- this=${this}  ")
        tasks.forEach { println("${it.name}") }
    }
}


/*
//Adding a dependency on a task
tasks.named("commontask0") { 	//Accessing a task via API -
    dependsOn("commontask3", "commontask2")
}

//Control order execution
tasks.named("commontask2") { mustRunAfter(tasks.named("commontask3")) }

//Modify an existing behaviour
val t2 = tasks.named("commontask2")

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
*/
/*
..\gradlew -q :taskAccess:commontask3

..\gradlew -q :taskAccess:commontask2

..\gradlew -q :taskAccess:commontask0

 */


