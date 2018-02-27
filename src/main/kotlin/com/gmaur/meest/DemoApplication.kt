package com.gmaur.meest

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
        val requestOrFailure = Meest.parseByCity(city)
        return requestOrFailure
                .map { it -> meest.request(it) }

                .bimap({
                    ResponseEntity.badRequest().body(map(it))
                }, { it -> it })
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