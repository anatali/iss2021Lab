plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    `maven-publish`

}
repositories {
    mavenCentral()
    //flatDir {   dirs( "C:\\Didattica2018Work\\iss2021Lab\\testBuilder\\buildSrc\\lib"	 ) }
    //flatDir {   dirs( "./lib"	 ) }
    flatDir {   dirs( "../../unibolibs"	 ) }
}

version = "2.0"

dependencies{
    // https://mvnrepository.com/artifact/it.unibo.alice.tuprolog/tuprolog
    //compile group: 'it.unibo.alice.tuprolog', name: 'tuprolog', version: '2.1.1'
    //implementation("it.unibo.alice.tuprolog:tuprolog:2.1.1")

    implementation( "tuprolog:2p301" )
    //implementation( "disiIss2020:unibo.disi.builder-1.0" )
}

gradlePlugin {
    plugins {
        create("myPlugins") {
            id = "disiPlugin"
            implementationClass = "unibo.disi.plugins.MyPlugin"
        }

        create("disiPlugins") {
            id = "disiBuilderJavaPlugin"
            implementationClass = "unibo.disi.plugins.DisiBuilderAsPlugin"
        }

        create("mydisiPlugins") {
            id = "disiBuilderKotlinPlugin"
            implementationClass = "DisiBuilderPlugin"
        }
        create("my2disiPlugins") {
            id = "mydisiBuilderKotlinPlugin"
            implementationClass = "MyDisiBuilderPlugin"
        }
    }
}