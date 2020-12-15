/*
See buildSrc
*/
/*
plugins{
    application
}

println(" ...... build in taskForBuildSrc sub-project  ")

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

val compileClasspath by configurations.creating
val runtimeClasspath by configurations.creating {
    extendsFrom(compileClasspath)
}
dependencies {
    findLibraries().forEach {
        compileClasspath(files(it))
        //compileOnly( files(it) )
    }
    runtimeClasspath(files("$buildDir/bin"))
    //runtimeOnly(files("$buildDir/bin"))
}
tasks.register<CompileJava>("compileJava")




dependencies {
    compileClasspath(project(":library")) {
        targetConfiguration = "runtimeClasspath"
    }
}
/*
tasks.compileJava {
    dependsOn(project(":library").tasks.compileJava)
    fromConfiguration(configurations.compileClasspath.get())
}
*/
tasks.register<RunJava>("runJava") {
    fromConfiguration(configurations.runtimeClasspath.get())
    mainClass = "P1"
}
/*
allprojects {
    tasks.register<Clean>("clean")
}

subprojects {
    val compileClasspath by configurations.creating
    val runtimeClasspath by configurations.creating {
        extendsFrom(compileClasspath)
    }
    dependencies {
        findLibraries().forEach {
            compileClasspath(files(it))
        }
        runtimeClasspath(files("$buildDir/bin"))
    }
    tasks.register<CompileJava>("compileJava")
}

*/