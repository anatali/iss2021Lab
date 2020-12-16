/*
See buildSrc
*/
println(" ...... build in taskForBuildSrc sub-project  ")

val compileClasspath by configurations.creating
val runtimeClasspath by configurations.creating {
    extendsFrom(compileClasspath)
}
dependencies {
    findLibraries().forEach {
        println("--------------------------------------- $it")
        compileClasspath(files(it))
        //compileOnly( files(it) )
    }
    println("--------------------------------------- $buildDir")
    runtimeClasspath(files("$buildDir/bin"))
    //runtimeOnly(files("$buildDir/bin"))
}
tasks.register<CompileJava>("compileJava")

tasks.register<RunJava>("runJava") {
    //fromConfiguration(configurations.runtimeClasspath.get())
    fromConfiguration( runtimeClasspath )
    mainClass = "P1"
}

/*
plugins{
    application
}



repositories {
    // Use JCenter for resolving dependencies.
    jcenter()
}

//buildscript{}
dependencies {
    // Use JUnit test framework.
    testImplementation("junit:junit:4.13")
    // This dependency is used by the application.
    implementation("com.google.guava:guava:29.0-jre")
}

application {
    // Define the main class for the application.
    mainClass.set("P1")
}

task<CommonTask>("ct"){
    msg ="Hello from ${this.name} in ${project.name}"
}

task<RunJava>("commonRunJava"){
    mainClass="P0"
}
*/






/*
dependencies {
    compileClasspath(project(":library")) {
        targetConfiguration = "runtimeClasspath"
    }
}




tasks.compileJava {
    dependsOn(project(":library").tasks.compileJava)
    fromConfiguration(configurations.compileClasspath.get())
}
*/

/*
allprojects {
    tasks.register<Clean>("clean")
}



*/