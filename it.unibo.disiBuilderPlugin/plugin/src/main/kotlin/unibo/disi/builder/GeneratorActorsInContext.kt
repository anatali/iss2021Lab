package unibo.disi.builder

import alice.tuprolog.Prolog

object  GeneratorActorsInContext {

    fun gen(  ctxName: String, sysKb : Prolog){
        val sol = sysKb.solve("getActorNames(ACTORS,$ctxName)." )
        if(  sol.isSuccess  ) {
            val actorNames     = sol.getVarValue("ACTORS") //List
            val actorNamesList = genUtils.strRepToList(actorNames.toString())
            actorNamesList.forEach{
                v -> genCodeActorFile(genUtils.genFilePathName(v), v, msgdriven=true) }
        }else println("generatorMsgDrivenSystem | gen  ERROR")
    }

    fun genCodeActorFile( filePathName: String, actorName : String, msgdriven : Boolean ){
        println( "generatorActors | genCodeActorFile actorName=$actorName filePathName=$filePathName")
        //generate a new directory
        val dirName = genUtils.genFilePathName(actorName)
        genUtils.genDirectory(dirName)
        val actorfName = "$dirName/${actorName}.kt"
        val actorf     = java.io.File( actorfName )
        if( actorf.exists() ) return
        println( "generatorMsgDrivenSystem | genCodeActorFile actorfName=$actorfName"  )
        var content : String
        if( msgdriven) content    = generatorActorMsgDrivenCode.gen(actorName)
        else           content    = GeneratorActorMsgBasedCode.gen(actorName)
        actorf.writeText( content )
        //println("generator | done msgdriven=$msgdriven genCodeActorFile for $actorName")
    }



}//generatorActors