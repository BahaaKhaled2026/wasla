package com.wasla.api

import org.springframework.boot.fromApplication
import org.springframework.boot.with


fun main(args: Array<String>) {
	fromApplication<WaslaApiApplication>().with(TestcontainersConfiguration::class).run(*args)
}
