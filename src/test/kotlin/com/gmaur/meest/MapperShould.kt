package com.gmaur.meest

import arrow.core.Either
import com.gmaur.meest.Meest.Mapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class) // this is necessary for infinitest
class MapperShould {

    private var mapper: Meest.Mapper? = null

    @Before
    @BeforeEach
    fun setup() {
        mapper = Mapper()
    }

    @Test
    fun `do not parse results when there are errors - 101`() {
        val message = "Ошибка авторизации"
        val serviceResponse = createResponse("101", message)

        val response = mapper?.map(serviceResponse.toEither())!!

        assertResponseIs(response, "Authentication Error", message)
    }

    @TestFactory
    fun translateDynamic(): List<DynamicTest> {
        val testCases = listOf(Triple("101", "Authentication Error", ""))
        return testCases.map {
            DynamicTest.dynamicTest(
                    " Test for translation " + " case ${it.first} == ${it.second}",
                    {
                        val message = "sample message"
                        val serviceResponse = createResponse(it.first, message)

                        val response = mapper?.map(serviceResponse.toEither())!!

                        assertResponseIs(response, it.second, message)
                    })
        }
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