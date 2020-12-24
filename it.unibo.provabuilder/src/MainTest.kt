import disi.builder.generator	

fun main() {	//args: Array<String>?
		//Utils.test()
		val path = System.getProperty("user.dir")
		println("path=:$path"  );
		
		generator.genCodeFromModel("demo0")

	}