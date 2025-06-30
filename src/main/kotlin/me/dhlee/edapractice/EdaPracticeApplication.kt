package me.dhlee.edapractice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.retry.annotation.EnableRetry
import org.springframework.scheduling.annotation.EnableAsync

@EnableRetry
@EnableAsync
@SpringBootApplication
class EdaPracticeApplication

fun main(args: Array<String>) {
    runApplication<EdaPracticeApplication>(*args)
}
