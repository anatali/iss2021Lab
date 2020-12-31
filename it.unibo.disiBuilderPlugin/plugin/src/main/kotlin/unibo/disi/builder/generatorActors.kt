package unibo.disi.builder

object  generatorActors {

    fun genActorsCode( ctxName : String, msgdriven : Boolean =true ){
        val sol = Generator.pengine.solve("getActorNames(ACTORS,$ctxName)." )
        if(  sol.isSuccess  ) {
            val actorNames     = sol.getVarValue("ACTORS") //List
            val actorNamesList = genUtils.strRepToList(actorNames.toString())
            actorNamesList.forEach{
                v -> genCodeActorFile(genUtils.genFilePathName(v), v, msgdriven) }
        }else println("generator | genActorsCode ERROR")
    }

    fun genCodeActorFile( filePathName: String, actorName : String, msgdriven : Boolean ){
        println( "generatorActors | genCodeActorFile actorName=$actorName filePathName=$filePathName")
        //generate a new directory
        val dirName = genUtils.genFilePathName(actorName)
        genUtils.genDirectory(dirName)
        val actorfName = "$dirName/${actorName}.kt"
        val actorf     = java.io.File( actorfName )
        if( actorf.exists() ) return
        println( "generatorActors | genCodeActorFile actorfName=$actorfName"  )
        var content = "todo"
        if( msgdriven) content    = generatorActorMsgDrivenCode.gen(actorName)
        else           content    = GeneratorActorMsgBasedCode.gen(actorName)
        actorf.writeText( content )
        //println("generator | done msgdriven=$msgdriven genCodeActorFile for $actorName")
    }

}//generatorActors