plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}
repositories {
    mavenCentral()
    //flatDir {   dirs( "C:\\Didattica2018Work\\iss2021Lab\\testBuilder\\buildSrc\\lib"	 ) }
    //flatDir {   dirs( "./lib"	 ) }
    flatDir {   dirs( "../../unibolibs"	 ) }
}

dependencies{
    // https://mvnrepository.com/artifact/it.unibo.alice.tuprolog/tuprolog
    //compile group: 'it.unibo.alice.tuprolog', name: 'tuprolog', version: '2.1.1'
    //implementation("it.unibo.alice.tuprolog:tuprolog:2.1.1")

    implementation( "tuprolog:2p301" )
    implementation( "disiIss2020:unibo.disi.builder-1.0" )
}

gradlePlugin {
    plugins {
        create("myPlugins") {
            id = "disiPlugin"
            implementationClass = "unibo.disi.plugins.MyPlugin"
        }
    }
}