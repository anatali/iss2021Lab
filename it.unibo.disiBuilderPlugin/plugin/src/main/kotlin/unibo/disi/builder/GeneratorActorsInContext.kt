package unibo.disi.builder

import alice.tuprolog.Prolog

object  GeneratorActorsInContext {

    fun gen(  ctxName: String, sysKb : Prolog, msgdriven : Boolean){
        val sol = sysKb.solve("getActorNames(ACTORS,$ctxName)." )
        if(  sol.isSuccess  ) {
            val actorNames     = sol.getVarValue("ACTORS") //List
            val actorNamesList = GenUtils.strRepToList(actorNames.toString())
            actorNamesList.forEach{
                v -> genCodeActorFile(GenUtils.genFilePathName(v), v, msgdriven) }
        }else println("GeneratorActorsInContext | gen  ERROR: no actors found")
    }

    fun genCodeActorFile( filePathName: String, actorName : String, msgdriven : Boolean ){
        println( "GeneratorActorsInContext | genCodeActorFile actorName=$actorName filePathName=$filePathName")
        //generate a new directory
        val dirName = GenUtils.genFilePathName(actorName)
        GenUtils.genDirectory(dirName)
        val actorfName = "$dirName/${actorName}.kt"
        val actorf     = java.io.File( actorfName )
        if( actorf.exists() ) return
        println( "GeneratorActorsInContext | genCodeActorFile actorfName=$actorfName"  )
        var content : String
        if( msgdriven) content    = generatorActorMsgDrivenCode.gen(actorName)
        else           content    = GeneratorActorMsgBasedCode.gen(actorName)
        actorf.writeText( content )
        //println("generator | done msgdriven=$msgdriven genCodeActorFile for $actorName")
    }
}//GeneratorActorsInContext