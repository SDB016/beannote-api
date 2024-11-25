package com.beannote.beannoteapi

import com.beannote.beannoteapi.config.TestcontainersConfiguration
import org.springframework.boot.fromApplication
import org.springframework.boot.with

fun main(args: Array<String>) {
    fromApplication<BeannoteApiApplication>().with(TestcontainersConfiguration::class).run(*args)
}
