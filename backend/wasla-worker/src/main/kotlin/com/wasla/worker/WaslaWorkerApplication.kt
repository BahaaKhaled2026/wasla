package com.wasla.worker

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WaslaWorkerApplication

fun main(args: Array<String>) {
	runApplication<WaslaWorkerApplication>(*args)
}
