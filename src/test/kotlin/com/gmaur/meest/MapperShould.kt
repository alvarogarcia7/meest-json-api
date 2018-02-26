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
        val message = "Ошибка авторизации"
        val serviceResponse = createResponse("101", message)

        val response = mapper.map(serviceResponse.toEither())

        assertResponseIs(response, "Authentication Error", message)
    }

    private fun createResponse(code: String, message: String): Response {
        val serviceResponse = Response()
        val message = message
        serviceResponse.errors = ResponseFactory.error(code, message)
        return serviceResponse
    }

    private fun assertResponseIs(response: Either<List<Error>, Results>, first: String, s: String) {
        assertThat(response).isEqualTo(
                Either.left(
                        listOf(
                                BusinessError.x(first,
                                        BusinessError.leaf(s)))))
    }

}