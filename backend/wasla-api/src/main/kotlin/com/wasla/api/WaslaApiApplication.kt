package com.wasla.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WaslaApiApplication

fun main(args: Array<String>) {
	runApplication<WaslaApiApplication>(*args)
}
