plugins {
    `kotlin-dsl`
}
repositories {
    mavenCentral()
    flatDir {   dirs( "./lib"	 ) }
}

dependencies{
    // https://mvnrepository.com/artifact/it.unibo.alice.tuprolog/tuprolog
    //compile group: 'it.unibo.alice.tuprolog', name: 'tuprolog', version: '2.1.1'
    implementation("it.unibo.alice.tuprolog:tuprolog:2.1.1")

}