package it.unibo.utils

object Utils {
	fun test() {
		println("Hello from Utils.kt")
	}

/*
 genFileDir
*/	
 fun genFileDir( dir: String,  packageName: String,  name: String,  suffix:String ,  contents: String){
   val sysName  = packageName.replace(".", "/")
   var fName    = ""
   var afterDot = ""
	 if( suffix.length > 0  ) afterDot = "." + suffix
	 if( sysName.length > 0 ) fName   = dir+"/"+sysName + "/" + name + afterDot
	 else fName   = dir+"/"+ name + afterDot
     println(" +++ generate " + fName); 
	 val fa = java.io.File( fName )
	 fa.writeText(contents)	  
}	

}//Object Utils

object generator{
	fun genSysRules(){
		Utils.genFileDir( "./src-gen",  "",  "sysRules" , "pl", builtin.sysRules )
	}
}//Object genRules

	fun main() {	//args: Array<String>?
		//Utils.test()
		val path = System.getProperty("user.dir")
		println(" +++ generate in dir:$path"  );
		generator.genSysRules();
	}