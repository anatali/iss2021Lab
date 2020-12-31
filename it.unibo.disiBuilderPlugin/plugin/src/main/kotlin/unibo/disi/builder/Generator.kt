package unibo.disi.builder
import alice.tuprolog.*

object Generator{

	val pengine     = Prolog()
	val userDir     =  System.getProperty("user.dir") //.replace("\\","//")
	val outSrcDir   =  "$userDir/src"
	val packagelogo = "it/unibo"
	var msgdriven   = false

	/*
     Generate the sysRules
    */
	fun genSysRules(){	//WE GENERATE A LOCAL COPY (for our generation ) AND THE GLOBAL ONE (for the user appl)
		genUtils.genFileDir( outSrcDir,  "",  "sysRules" , "pl", builtin.sysRules )
		genUtils.genFileDir( "./",  "",  "sysRules" , "pl", builtin.sysRules )
	}

	/*
     Generate the gradle build file
    */
	fun genGradleBuild( sysName: String ){
		genUtils.genFileDir( "./",  "",  "build" , "gradle", builtin.genGradleRules(sysName) )
	}

 	fun getFirstCtxName() : String {
		val sol = pengine.solve("context(NAME,HOST,PROTOCOL,PORT)." )
		if(  sol.isSuccess  ) {
			val ctxName = sol.getVarValue("NAME").toString()
			val ctxHost = sol.getVarValue("HOST").toString()
			//val ctxPort = sol.getVarValue("PORT").toString()
			println("ctxName=${ctxName} ctxHost=${ctxHost}")
 			return ctxName
		}
		else{ throw Exception("generator | ctx not found") }
	}

 	fun genCodeFromModel(modelFileName : String){
 		val path = System.getProperty("user.dir")
		println("generator | START path=:$path"  );
	//GENERATE THE OUTPUT DIRECTORY, if it does not exist
		val dirName = genUtils.genFilePathName(outSrcDir)
		genUtils.genDirectory(dirName)

	//GENERATE THE GRADLE BUILD FILE
		genGradleBuild( modelFileName )
	//GENERATE THE SYSTEM RULES (written in Prolog)
		genSysRules()	//genera sysRules.pl che poi VIENE USATO QUI SOTTO

	//LOAD THE MODEL IN THE LOCAL KB
		pengine.solve("consult('$modelFileName.pl')." )
	//LOAD THE SYSTEM RULES	IN THE LOCAL KB
		pengine.solve("consult( '$outSrcDir/sysRules.pl' )." )
	//GET THE ACTOR BEHAVIOUR MODEL (msgdriven or  msgbased)
		val sol = pengine.solve("system(SYSNAME,BEHAVIOUR)." )
		if(  sol.isSuccess  ) {
			val behaviour = sol.getVarValue("BEHAVIOUR").toString()
			msgdriven = (behaviour == "msgdriven")
			println("generator | genCodeFromModel msgdriven=$behaviour")
			if (msgdriven) {  //GENERATE THE SKELETON CODE OF THE MSG-DRIVEN KOTLIN ACTORS
				generatorActors.genActorsCode(getFirstCtxName(), msgdriven)
			} else {
				generatorMsgBasedSystem.gen( modelFileName )
			}
		}//success
		println("generator | END")
	}

}//Object generator

	fun main() {	//args: Array<String>?
		//Utils.test()
		val path = System.getProperty("user.dir")
		println("path=:$path"  );
		Generator.genCodeFromModel("demo0")

	}