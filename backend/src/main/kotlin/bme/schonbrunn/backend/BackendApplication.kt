package bme.schonbrunn.backend

import bme.schonbrunn.backend.parser.NativeParserDriver
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BackendApplication

fun main(args: Array<String>) {
    NativeParserDriver // Initialize native parser library

    val caff = NativeParserDriver.parse("1.caff")
    println(caff)
    val preview = NativeParserDriver.preview("1.caff", "test.gif")
    println(preview)

    runApplication<BackendApplication>(*args)
}
