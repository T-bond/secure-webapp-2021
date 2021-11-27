package bme.schonbrunn.backend.parser

object NativeParserDriver {

    init {
        println(System.getProperty("user.dir") + "\\" + libName())
        System.loadLibrary(System.getProperty("user.dir") + "\\" + libName())

    }

    external fun parse(file: String): CAFF?
    external fun preview(input: String, output: String): Boolean

    private fun libName(): String {
        val os = System.getProperty("os.name").lowercase()
        return when {
            os.contains("win") -> {
                "parser.dll"
            }
            os.contains("nix") || os.contains("nux") || os.contains("aix") -> {
                "libparser.so"
            }
            os.contains("mac") -> {
                "libparser.dylib"
            }
            else -> "libparser"
        }
    }

}