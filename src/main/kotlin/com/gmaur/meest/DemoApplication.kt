package com.gmaur.meest

import arrow.core.flatMap
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackageClasses = [DroppointsController::class])
class DemoApplication

fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
}


@RestController
@Configuration
class DroppointsController(private val meest: Meest) {

    @GetMapping("/droppoints")
    fun rebalance(@RequestParam("city") city: String): Any {
        val result = Meest.parseByCity(city)
                .mapLeft { it.map { it as BusinessError } }
                .flatMap { meest.request(it) }
                .bimap({
                    ResponseEntity.status(it.first().code).body(map(it))
                }, {
                    ResponseEntity.ok().body(it)
                })
        if (result.isLeft()) {
            return result.swap().get()
        } else {
            return result.get()
        }
    }

    private fun map(errors: List<Error>): Any {
        return ErrorX.Collection(errors.map { ErrorX(it.message!!) })
    }

    data class ErrorX(val message: String) {
        data class Collection(val values: List<ErrorX>)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun illegalArgumentException() {
    }
}