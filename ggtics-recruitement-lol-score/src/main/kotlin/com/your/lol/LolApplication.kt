package com.your.lol

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LolApplication

fun main(args: Array<String>) {
	runApplication<LolApplication>(*args)
}
