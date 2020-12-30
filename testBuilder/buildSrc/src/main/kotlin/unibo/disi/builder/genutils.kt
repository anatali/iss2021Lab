package unibo.disi.builder

import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import alice.tuprolog.*

object genUtils {

	fun test() {
		println("Hello from Utils.kt")
	}

fun genFileName( dir: String,  packageName: String,  name: String,  suffix:String ) : String {
   val sysName  = packageName.replace(".", "/")
   var fName    = " "
   var afterDot = ""
	 if( suffix.length > 0  ) afterDot = "." + suffix
	 if( sysName.length > 0 ) fName   = dir+"/"+sysName + "/" + name + afterDot
	 else fName   = dir+"/"+ name + afterDot
     //println(" +++ generate " + fName)
	 return fName
}

 fun genFileDir( dir: String,  packageName: String,  name: String,  suffix:String ,  contents: String){
	val fName    = genFileName(dir, packageName, name, suffix )
	val fa       = java.io.File( fName )
	if( fa.exists() ) return
	fa.writeText(contents)	  
}

	fun genDirectory( dirName: String   ){
		val folder  = File(dirName)
		if( folder.exists() ) return
		else{
			println( "generator | genMainCtxFile created dir=$dirName"  )
			folder.mkdirs()
		}
	}
	
	fun copyFile(dir: String,  packageName: String,  name: String,  suffix:String){
		val sourcefName    = genFileName(dir, packageName, name, suffix )
		val fsource        = java.io.File( sourcefName )
		println( sourcefName )
		val contents = fsource.readText()
		
		val destfName      = genFileName("./src-gen",  "", name, suffix )
		println( destfName )
		val fdest          = java.io.File( destfName )
		fdest.writeText(contents)
	}	

	
	fun strRepToList( lr: String ) : List<String>{
		return lr.replace("[","").replace("]","").split(",")
	}

}//Object genUtils

