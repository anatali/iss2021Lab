/*
tasks.register("brokenTask") { // creates a new task
	println("this is executed at CONFIGURATION time!")
}
*/

tasks.register("helloWorld") {
	doLast { // This method takes as argument a Task.() -> Unit
		println("Hello, World!")
	}
}

tasks.getByName("helloWorld") { // let's find an existing task
	doFirst { // Similar to doLast, but adds operations in head
		println("Configured later, executed first.")
	}
}