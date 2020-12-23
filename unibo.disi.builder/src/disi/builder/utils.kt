package disi.builder

object genUtils {
	fun test() {
		println("Hello from Utils.kt")
	}

fun genFileName( dir: String,  packageName: String,  name: String,  suffix:String ) : String {
   val sysName  = packageName.replace(".", "/")
   var fName    = ""
   var afterDot = ""
	 if( suffix.length > 0  ) afterDot = "." + suffix
	 if( sysName.length > 0 ) fName   = dir+"/"+sysName + "/" + name + afterDot
	 else fName   = dir+"/"+ name + afterDot
     //println(" +++ generate " + fName)
	 return fName
}
/*
 genFileDir
*/	
 fun genFileDir( dir: String,  packageName: String,  name: String,  suffix:String ,  contents: String){
	/*
   val sysName  = packageName.replace(".", "/")
   var fName    = ""
   var afterDot = ""
	 if( suffix.length > 0  ) afterDot = "." + suffix
	 if( sysName.length > 0 ) fName   = dir+"/"+sysName + "/" + name + afterDot
	 else fName   = dir+"/"+ name + afterDot
     println(" +++ generate " + fName);
 */
	val fName    = genFileName(dir, packageName, name, suffix )
	val fa       = java.io.File( fName )
	fa.writeText(contents)	  
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

}//Object Utils

object disigenerator{
	fun genSysRules(){
		genUtils.genFileDir( "./src-gen",  "",  "sysRules" , "pl", builtin.sysRules )
		//println( genUtils.genFileName("./src",  "disi/kb",  "sysRules" , "pl")  )
		//println( genUtils.genFileName("./src.gen",  "",  "sysRules" , "pl")  )
		//genUtils.copyFile(  "./src",  "disi/kb",  "sysRules" , "pl"  )		
	}
	
	fun genCtxMain(name: String){
		val fname = genUtils.genFileName( "./src-gen",  "it.unibo.$name",  "Main$name" , "kt"  )
		println( "disigenerator |  genCtxMain fname=$fname" )
	}
}//Object genRules

	fun main() {	//args: Array<String>?
		//Utils.test()
		val path = System.getProperty("user.dir")
		println("path=:$path"  );
		disigenerator.genSysRules();
	}