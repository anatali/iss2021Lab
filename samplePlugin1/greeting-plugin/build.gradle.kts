plugins {
    // Apply the Java Gradle plugin development plugin to add support for developing Gradle plugins
    `java-gradle-plugin`
    `maven-publish`
}

repositories {
    // Use jcenter for resolving dependencies
    jcenter()
}

dependencies {
    // Use JUnit test framework for unit tests
    testImplementation("junit:junit:4.13")
}

gradlePlugin {
    // Define the plugin
    val greeting by plugins.creating {
        id = "com.example.plugin.greeting"
        implementationClass = "com.example.plugin.GreetingPlugin"
    }
}

// Add a source set and a task for a functional test suite
val functionalTest by sourceSets.creating
gradlePlugin.testSourceSets(functionalTest)

configurations[functionalTest.implementationConfigurationName].extendsFrom(configurations.testImplementation.get())

val functionalTestTask = tasks.register<Test>("functionalTest") {
    testClassesDirs = functionalTest.output.classesDirs
    classpath = configurations[functionalTest.runtimeClasspathConfigurationName] + functionalTest.output
}

tasks.check.configure {
    // Run the functional tests as part of `check`
    dependsOn(functionalTestTask)
}
//------------------------------------------------------
group = "com.example"
version = "1.0.0"

gradlePlugin {
    plugins {
        create("hello") {
            id = "com.example.hello"
            implementationClass = "com.example.plugin.GreetingPlugin"
        }

    }
}

publishing {
    repositories {
        maven {
            url = uri("../../uniboRepos/maven-repo")
        }
    }
}