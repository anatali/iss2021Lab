package unibo.disi.builder

import alice.tuprolog.Prolog
import java.io.File

object  GeneratorActorsInContext {

    fun gen(  systemName: String, ctxName: String, sysKb : Prolog, msgdriven : Boolean){
        var content : String
        if( msgdriven) {
            content  = GeneratorActorMsgDrivenCode.gen( systemName, ctxName, sysKb )
            genCodeActorFile( systemName, content )
        }else{ //fsm
            val actorNamesList = GenUtils.getActorNames(ctxName,sysKb)
            actorNamesList.forEach{ actorName ->
                content = GeneratorActorFsmCode.gen(actorName)
                genCodeActorFile( actorName, content)
            }
        }
     }


    fun genCodeActorFile(  actorName : String, content: String )   {
        println( "GeneratorActorsInContext | genCodeActorFile actorName=$actorName  ")
        val dirName    = GenUtils.genFilePathName(actorName)
        val actorfName = "$dirName/${actorName}.kt"
        val actorf     =  File( actorfName )
        if( actorf.exists() ) {
            println( "GeneratorActorsInContext | WARNING actorName $actorName ALREADY EXISTS  ")
        }
        else{ //the actor file does not exist
            GenUtils.genDirectory(dirName)
             actorf.writeText( content )
        }
    }
}//GeneratorActorsInContext