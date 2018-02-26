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
        val errors = error("101")
        serviceResponse.errors = errors

        val response = mapper.map(serviceResponse)

        assertThat(response).isEqualTo(left(listOf(BusinessError("Authentication Error"))))
    }

    private fun error(code: String): Response.Errors {
        val errors = Response.Errors()
        errors.code = code
        errors.name = "Ошибка авторизации"
        return errors
    }
}