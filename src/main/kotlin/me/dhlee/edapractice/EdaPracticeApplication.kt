package me.dhlee.edapractice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@EnableAsync
@SpringBootApplication
class EdaPracticeApplication

fun main(args: Array<String>) {
    runApplication<EdaPracticeApplication>(*args)
}
