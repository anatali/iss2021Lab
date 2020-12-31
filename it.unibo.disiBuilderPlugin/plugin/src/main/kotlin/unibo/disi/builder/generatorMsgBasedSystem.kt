package unibo.disi.builder

object generatorMsgBasedSystem {

    fun gen(modelFileName : String  ){
        //For a msg-based (qak) actor system we might have N>1 contexts
        val ctxNamesList = getCtxNames() //List<String>
        ctxNamesList.forEach{ ctxName ->
            generatorCtx.genTheContextCode(ctxName, modelFileName, false)
        }
    }

    fun getCtxNames() : List<String>{
        val sol = Generator.pengine.solve("getCtxNames(CTXNAMES)." )
        if(  sol.isSuccess  ) {
            val ctxNames = sol.getVarValue("CTXNAMES") 	//List
            val ctxNamesList = genUtils.strRepToList(ctxNames.toString())
            return ctxNamesList
        }else throw Exception("")
    }

}//generatorMsgBasedSystem
