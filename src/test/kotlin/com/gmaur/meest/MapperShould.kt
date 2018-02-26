package com.gmaur.meest

import arrow.core.Either.Companion.left
import com.gmaur.meest.Meest.Mapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class MapperShould {

    private lateinit var mapper: Meest.Mapper

    @Before
    fun setup() {
        mapper = Mapper()
    }

    @Test
    fun `do not parse results when there are errors`() {
        val serviceResponse = Response()
        val message = "Ошибка авторизации"
        val errors = error("101", message)
        serviceResponse.errors = errors

        val response = mapper.map(serviceResponse)

        assertThat(response).isEqualTo(left(listOf(
                BusinessError.x("Authentication Error", BusinessError.leaf(message)))))
    }

    private fun error(code: String, message: String): Response.Errors {
        val errors = Response.Errors()
        errors.code = code
        errors.name = message
        return errors
    }
}