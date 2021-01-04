package unibo.disi.builder

import alice.tuprolog.Prolog

object GeneratorFsmSystem {

    fun gen( systemName: String, modelFileName : String, sysKb : Prolog){
        //For a msg-based actor system we might have N>1 contexts
        val ctxNamesList = getCtxNames(sysKb) //List<String>
        ctxNamesList.forEach{ ctxName ->
            GeneratorCtx.genTheContextCode(systemName,ctxName, modelFileName, sysKb, false )
        }
    }

    fun getCtxNames( sysKb : Prolog ) : List<String>{
        val sol = sysKb.solve("getCtxNames(CTXNAMES)." )
        if(  sol.isSuccess  ) {
            val ctxNames     = sol.getVarValue("CTXNAMES") 	//List
            val ctxNamesList = GenUtils.strRepToList(ctxNames.toString())
            return ctxNamesList
        }else throw Exception("")
    }

}//generatorMsgBasedSystem
