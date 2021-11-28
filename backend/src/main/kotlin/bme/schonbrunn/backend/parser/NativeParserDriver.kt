package bme.schonbrunn.backend.parser

import java.io.File

object NativeParserDriver {

    init {
        try {
            System.loadLibrary("parser")
        } catch (ignored: UnsatisfiedLinkError) {
            val workdir = System.getProperty("user.dir")
            val libsDir = File.separatorChar + "libs" + File.separatorChar

            System.load(workdir + libsDir + libName())
        }
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