package unibo.disi.builder

import alice.tuprolog.Prolog

object GeneratorFsmSystem {

    fun gen( systemName: String, modelFileName : String, sysKb : Prolog){
        //For a msg-based actor system we might have N>1 contexts
        val ctxNamesList = GenUtils.getCtxNames(sysKb) //List<String>
        ctxNamesList.forEach{ ctxName ->
            GeneratorCtx.genTheContextCode(systemName,ctxName, modelFileName, sysKb, false )
        }
    }
}//GeneratorFsmSystem
