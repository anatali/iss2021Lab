import org.gradle.internal.impldep.org.mozilla.javascript.tools.shell.Global.readline

println(" ...... build in taskForBuildscript sub-project  ")

/*
In this subproject, ...
 */
buildscript {
    repositories {
        //mavenCentral()
        //jcenter()
        mavenLocal()
        flatDir {   dirs("../unibolibs")	 }
        //flatDir {   dirs("C:\\tmp\\repo")	 }
    }
    // everything listed in the dependencies is actually a plugin,
    // which we'll do "apply plugin" in our module level gradle file.
    //https://medium.com/@StefMa/its-time-to-ditch-the-buildscript-block-a1ab12e0d9ce
    dependencies {
        classpath( "disi:unibo.disi.builder-1.0" )
        //classpath( "unibo.disi:it.unibo.disiPlugin:1.0" )
    }


}
/*
buildscript {
    repositories {
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.30-M1")
    }
}

apply(plugin = "org.jetbrains.kotlin.jvm")
*/

/*
apply {
    plugin("java")
    plugin("org.jetbrains.kotlin.jvm:1.3.72")
}
*/

/*
apply{
        plugin("unibo.disi.plugins.MyPlugin")
}
*/


task("hellobs") {
    println(" .......... hellobs")
    doLast {	//a shortcut to define an action
        println("Hello world from taskForBuildscript - this=${this}")
        //val v = disi.builder.test()
        //println(v)
    }
}

/*
task("hellobs") {
    println(" .......... hellobs")
    doLast {	//a shortcut to define an action
        println("Hello world from taskForBuildscript - this=${this}")
        val v = generator.genFilePathName("xxx")
        //println(v)
    }
}*/