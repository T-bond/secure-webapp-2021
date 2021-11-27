package bme.schonbrunn.backend.parser

object NativeParserDriver {

    init {
        System.load(System.getProperty("user.dir") + "/libparser.so")
    }

    external fun parse(file: String): CAFF?
    external fun preview(input: String, output: String): Boolean

}