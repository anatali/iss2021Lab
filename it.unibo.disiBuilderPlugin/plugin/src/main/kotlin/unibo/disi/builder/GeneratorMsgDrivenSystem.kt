package unibo.disi.builder
import alice.tuprolog.Prolog

object  GeneratorMsgDrivenSystem {

    fun gen(  systemName: String, sysKb : Prolog ){
        val ctxName = getFirstCtxName( sysKb )
        GeneratorActorsInContext.gen(systemName, ctxName, sysKb, true )  //Gen the code of the actors
     }

    private fun getFirstCtxName( sysKb : Prolog ) : String {
        val sol = sysKb.solve("context(NAME,HOST,PROTOCOL,PORT)." )
        if(  sol.isSuccess  ) {
            val ctxName = sol.getVarValue("NAME").toString()
            val ctxHost = sol.getVarValue("HOST").toString()
            //val ctxPort = sol.getVarValue("PORT").toString()
            println("generatorMsgDrivenSystem | ctxName=${ctxName} ctxHost=${ctxHost}")
            return ctxName
        }
        else{ throw Exception("generatorMsgDrivenSystem | ctx not found") }
    }


}//generatorActors