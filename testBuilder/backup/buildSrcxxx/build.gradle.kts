plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    `maven-publish`

}
repositories {
    mavenLocal()        //MUST BE THE FIRST
    mavenCentral()
    flatDir {   dirs( "../../unibolibs"	 ) }
}

group   = "it.unibo.disi"
version = "3.5"

println("Project.GRADLE_PROPERTIES = ${ Project.GRADLE_PROPERTIES }")

dependencies{
    // https://mvnrepository.com/artifact/it.unibo.alice.tuprolog/tuprolog
    //compile group: 'it.unibo.alice.tuprolog', name: 'tuprolog', version: '2.1.1'
    //implementation("it.unibo.alice.tuprolog:tuprolog:2.1.1")

    implementation( "tuprolog:2p301" )
    //implementation( "disiIss2020:unibo.disi.builder-1.0" )
}

gradlePlugin {
    plugins {
        /*
        create("myPlugins") {
            id = "unibo.disi.disiPlugin"
            implementationClass = "unibo.disi.plugins.MyPlugin"
        }

        create("disiPlugins") {
            id = "unibo.disi.disiBuilderJavaPlugin"
            implementationClass = "unibo.disi.plugins.DisiBuilderAsPlugin"
        }

        create("mydisiPlugins") {
            id = "unibo.disi.disiBuilderKotlinPlugin"
            implementationClass = "DisiBuilderPlugin"
        }

         */
        create("my2disiPlugins") {
            id = "$group.mydisiBuilderKotlinPlugin"
            implementationClass = "MyDisiBuilderPlugin"
        }
    }
}
/*
publishing{
    publications {

        create<MavenPublication>("disi") {
            groupId = "unibo.disi"
            artifactId = "builder"
            version = "4.0"
            from(components["java"])
        }
    }

}
*/

