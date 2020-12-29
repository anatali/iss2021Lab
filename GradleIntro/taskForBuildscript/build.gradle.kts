import org.gradle.internal.impldep.org.mozilla.javascript.tools.shell.Global.readline

println(" ...... build in taskForBuildscript sub-project  ")
/*
plugins{    //specifying plugin by its id - you need to create a plugin id.
    id("it.unibo.disi.mydisiBuilderKotlinPlugin") version "3.5"
}
repositories {
    mavenLocal()
}
*/



/*
In this subproject, ...
 */

buildscript {
    repositories {
        //mavenCentral()
        //jcenter()
        mavenLocal()
        //flatDir {   dirs("../unibolibs")	 }
     }
    // everything listed in the dependencies is actually a plugin,
    // which we'll do "apply plugin" in our module level gradle file.
    //https://medium.com/@StefMa/its-time-to-ditch-the-buildscript-block-a1ab12e0d9ce
    dependencies {
        // ~/.m2/repository/com/company/product/plugin/product-gradle-plugin/1.0/product-gradle-plugin-1.0.jar
        //classpath 'com.company.product.plugin:product-gradle-plugin:1.0'

        //~/.m2/repository/mydisiBuilderKotlinPlugin/mydisiBuilderKotlinPlugin.gradle.plugin/2.0
        //classpath( "mydisiBuilderKotlinPlugin:mydisiBuilderKotlinPlugin.gradle.plugin:2.0" )
    }

    apply(plugin = "it.unibo.disi.mydisiBuilderKotlinPlugin")  //not found

}


//apply { plugin("mydisiBuilderKotlinPlugin")  }
//apply(plugin = "mydisiBuilderKotlinPlugin") //specifying  plugin by its class name
/*
1. You can code it directly within your Gradle build script.
2. You can put it under buildSrc (ex. buildSrc/src/main/groovy/MyCustomPlugin).
3. You can import your custom plugin as a jar in your buildscript method.
*/


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