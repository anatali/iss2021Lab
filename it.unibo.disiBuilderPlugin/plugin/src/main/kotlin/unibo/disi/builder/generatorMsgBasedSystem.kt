package unibo.disi.builder

import alice.tuprolog.Prolog

object generatorMsgBasedSystem {

    fun gen( modelFileName : String, sysKb : Prolog){
        //For a msg-based actor system we might have N>1 contexts
        val ctxNamesList = getCtxNames(sysKb) //List<String>
        ctxNamesList.forEach{ ctxName ->
            GeneratorCtx.genTheContextCode(ctxName, modelFileName, sysKb )
        }
    }

    fun getCtxNames( sysKb : Prolog ) : List<String>{
        val sol = sysKb.solve("getCtxNames(CTXNAMES)." )
        if(  sol.isSuccess  ) {
            val ctxNames = sol.getVarValue("CTXNAMES") 	//List
            val ctxNamesList = genUtils.strRepToList(ctxNames.toString())
            return ctxNamesList
        }else throw Exception("")
    }

}//generatorMsgBasedSystem
