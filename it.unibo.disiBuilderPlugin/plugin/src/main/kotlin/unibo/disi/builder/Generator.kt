package unibo.disi.builder
import alice.tuprolog.*

object Generator{

	val sysKb       = Prolog()
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


 	fun genCodeFromModel(modelFileName : String){
 		val path = System.getProperty("user.dir")
		println("Generator | START path=:$path modelFileName=$modelFileName"  );
	//GENERATE THE OUTPUT DIRECTORY, if it does not exist
		val dirName = genUtils.genFilePathName(outSrcDir)
		genUtils.genDirectory(dirName)
	//GENERATE THE GRADLE BUILD FILE
		genGradleBuild( modelFileName )
	//GENERATE THE SYSTEM RULES (written in Prolog)
		genSysRules()	//genera sysRules.pl che poi VIENE USATO QUI SOTTO
	//LOAD THE MODEL IN THE LOCAL KB
		sysKb.solve("consult('$modelFileName.pl')." )
	//LOAD THE SYSTEM RULES	IN THE LOCAL KB
		sysKb.solve("consult( '$outSrcDir/sysRules.pl' )." )
	//GET THE ACTOR BEHAVIOUR MODEL (msgdriven or  msgbased)
		val sol = sysKb.solve("system(SYSNAME,BEHAVIOUR)." )
		if(  sol.isSuccess  ) {
			val behaviour = sol.getVarValue("BEHAVIOUR").toString()
			println("Generator | genCodeFromModel $modelFileName behaviour=$behaviour")
			msgdriven = (behaviour == "msgdriven")
			if (msgdriven) {
				GeneratorMsgDrivenSystem.gen( sysKb ) //ONE Context only
			} else {
				generatorMsgBasedSystem.gen( modelFileName, sysKb ) //Many contexts possible
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