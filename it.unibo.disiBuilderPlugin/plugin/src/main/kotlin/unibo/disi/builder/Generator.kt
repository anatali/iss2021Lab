package unibo.disi.builder
import alice.tuprolog.*

object Generator{

	val sysKb       =  Prolog()
	val userDir     =  System.getProperty("user.dir") //.replace("\\","//")
	val outSrcDir   =  "$userDir/src"
	val packagelogo = "it/unibo"
	var msgdriven   =  false

	/*
     Generate the sysRules
    */
	fun genSysRules(){	//WE GENERATE A LOCAL COPY (for our generation ) AND THE GLOBAL ONE (for the user appl)
		//genUtils.genFileDir( outSrcDir,  "",  "sysRules" , "pl", builtin.sysRules )
		GenUtils.genFileDir( "./",  "",  "sysRules" , "pl", Builtin.sysRules )
		//TODO: copy sysRules.pl from the jar to the userDir
		//genUtils.copyFile( "./src", userDir, "",  "sysRules", "pl")
	}
	/*
     Generate the gradle build file
    */
	fun genGradleBuild( sysName: String ){
		GenUtils.genFileDir( "./",  "",  "build" , "gradle", Builtin.genGradleRules(sysName) )
	}


 	fun genCodeFromModel(modelFileName : String){
 		val path = System.getProperty("user.dir")
		println("Generator | START path=:$path modelFileName=$modelFileName"  );
	//GENERATE THE OUTPUT DIRECTORY, if it does not exist
		val dirName = GenUtils.genFilePathName(outSrcDir)
		GenUtils.genDirectory(dirName)
	//GENERATE THE GRADLE BUILD FILE
		genGradleBuild( modelFileName )
	//GENERATE THE SYSTEM RULES (written in Prolog)
		genSysRules()	//genera sysRules.pl in the user ws, che poi VIENE USATO QUI SOTTO
	//LOAD THE MODEL IN THE LOCAL KB
		sysKb.solve("consult('$modelFileName.pl')." )
	//LOAD THE SYSTEM RULES	IN THE LOCAL KB
		sysKb.solve("consult( './sysRules.pl' )." )
	//GET THE ACTOR BEHAVIOUR MODEL (msgdriven or  msgbased)
		val sol = sysKb.solve("system(SYSNAME,BEHAVIOUR)." )
		if(  sol.isSuccess  ) {
			val behaviour = sol.getVarValue("BEHAVIOUR").toString()
			println("Generator | genCodeFromModel $modelFileName , behaviour=$behaviour")
			msgdriven = (behaviour == "msgdriven")
			if (msgdriven) {
				GeneratorMsgDrivenSystem.gen( sysKb ) //ONE Context only
			} else {
				GeneratorMsgBasedSystem.gen( modelFileName, sysKb ) //Many contexts possible
			}
		}//success
		println("Generator | END")
	}

}//Object Generator

	fun main() {	//args: Array<String>?
		//Utils.test()
		val path = System.getProperty("user.dir")
		println("path=:$path"  );
		Generator.genCodeFromModel("demo0")

	}