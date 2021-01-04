package unibo.disi.builder

import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import alice.tuprolog.*

object GenUtils {

	fun test() {
		println("Hello from Utils.kt")
	}

	fun genFilePathName(fName: String) : String{
		return "${Generator.outSrcDir}/${Generator.packagelogo}/$fName"
	}

fun genFileName( dir: String,  packageName: String,  name: String,  suffix:String ) : String {
   val sysName  = packageName.replace(".", "/")
   var fName : String
   var afterDot = ""
	 if( suffix.length > 0  ) afterDot = "." + suffix
	 if( sysName.length > 0 ) fName   = dir+"/"+sysName + "/" + name + afterDot
	 else fName   = dir+"/"+ name + afterDot
     //println(" +++ generate " + fName)
	 return fName
}

 fun genFileDir( dir: String,  packageName: String,  name: String,  suffix:String ,  contents: String){
	val fName    = genFileName(dir, packageName, name, suffix )
	 println( "GenUtils | genFileDir fName=$fName"  )
	val fa       = java.io.File( fName )
	if( fa.exists() ) return
	fa.writeText(contents)	  
}

	fun genDirectory( dirName: String ) {
		val folder  = File(dirName)
		if( folder.exists() ){
			println( "GenUtils | WARNING: directory $dirName ALREADY EXISTS"  )
			folder.delete()
		}
		//else{
			println( "GenUtils | genDirectory created dir=$dirName"  )
			folder.mkdirs()
		//}
	}
	
	fun copyFile(sourcedir: String,  destdir: String, packageName: String,  name: String,  suffix:String){
		val sourcefName    = genFileName(sourcedir, packageName, name, suffix )
		val fsource        = java.io.File( sourcefName )
		println( "GenUtils | copyFile sourcefName=$sourcefName" )
		val contents = fsource.readText()

		val destfName      = genFileName(destdir,  "", name, suffix )
		println( "GenUtils | copyFile destfName=$destfName" )
		val fdest          = java.io.File( destfName )
		fdest.writeText(contents)
	}	

	
	fun strRepToList( lr: String ) : List<String>{
		return lr.replace("[","").replace("]","").split(",")
	}

	fun writeOnFile( actorfName : String, content: String){
		try {
			println("GenUtils | writeOnFile actorfName=$actorfName")
			val actorf = java.io.File(actorfName)
			if (actorf.exists()) return
			actorf.writeText(content)
		}catch( e: Exception){
			println("GenUtils | writeOnFile actorfName=$actorfName ERROR")
		}
	}

	fun getActorNames(ctxName: String, sysKb : Prolog) : List<String>{
		val sol = sysKb.solve("getActorNames(ACTORS,$ctxName).")
		if (sol.isSuccess) {
			val actorNames     = sol.getVarValue("ACTORS") //List
 			return strRepToList(actorNames.toString())
		} else{
			println("GenUtils | getActorNames  WARNING: no actors found")
			return listOf()
		}
	}

	fun getCtxNames( sysKb : Prolog ) : List<String>{
		val sol = sysKb.solve("getCtxNames(CTXNAMES)." )
		if(  sol.isSuccess  ) {
			val ctxNames     = sol.getVarValue("CTXNAMES") 	//List
 			return strRepToList( ctxNames.toString() )
		}else{
			println("GenUtils | getActorNames  WARNING: no context found")
			throw Exception("")
		}
	}


}//Object GenUtils

