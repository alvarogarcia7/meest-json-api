package com.gmaur.meest

import arrow.core.Either
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
    fun `do not parse results when there are errors - 101`() {
        val serviceResponse = Response()
        serviceResponse.errors = ResponseFactory.error("101", "Ошибка авторизации")

        val response = mapper.map(serviceResponse.toEither())

        assertThat(response).isEqualTo(Either.left(listOf(
                BusinessError.x("Authentication Error", BusinessError.leaf("Ошибка авторизации")))))
    }

}