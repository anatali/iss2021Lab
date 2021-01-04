package unibo.disi.builder

import alice.tuprolog.Prolog

object  GeneratorActorsInContext {

    fun gen(  systemName: String, ctxName: String, sysKb : Prolog, msgdriven : Boolean){
        /*
        val sol = sysKb.solve("getActorNames(ACTORS,$ctxName)." )
        if(  sol.isSuccess  ) {
            val actorNames     = sol.getVarValue("ACTORS") //List
            val actorNamesList = GenUtils.strRepToList(actorNames.toString())
            actorNamesList.forEach{
                v -> genCodeActorFile(GenUtils.genFilePathName(v), v, msgdriven) }
        }else println("GeneratorActorsInContext | gen  ERROR: no actors found")*/
        var content : String
        if( msgdriven) {
            content  = generatorActorMsgDrivenCode.gen( systemName, ctxName, sysKb )
            genCodeActorFile( systemName, content )
        }
        /*
        val actorNamesList = GenUtils.getActorNames(ctxName,sysKb)
        actorNamesList.forEach{v->genCodeActorFile(GenUtils.genFilePathName(v), v, msgdriven,sysKb)}

         */

    }

    fun genCodeActorFile(  actorName : String, content: String ){
        println( "GeneratorActorsInContext | genCodeActorFile actorName=$actorName  ")
        //generate a new directory
        val dirName = GenUtils.genFilePathName(actorName)
        GenUtils.genDirectory(dirName)
        val actorfName = "$dirName/${actorName}.kt"
        val actorf     = java.io.File( actorfName )
        if( actorf.exists() ) return
        println( "GeneratorActorsInContext | genCodeActorFile actorfName=$actorfName"  )
        //var content : String
        //if( msgdriven) content    = generatorActorMsgDrivenCode.gen(actorName,sysKb)
        //else
            //content    = GeneratorActorMsgBasedCode.gen(actorName)
        actorf.writeText( content )
        //println("generator | done msgdriven=$msgdriven genCodeActorFile for $actorName")
    }
}//GeneratorActorsInContext