package com.gmaur.meest

import arrow.core.Either
import com.gmaur.meest.Meest.Mapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
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

    @TestFactory
    fun mapResponses(): List<DynamicTest> {
        val testCases = listOf(
                Triple("100", "Connection Error", 500),
                Triple("101", "Authentication Error", 401),
                Triple("102", "Function is not found", 400),
                Triple("103", "Document not found", 400),
                Triple("104", "Directory not found", 400),
                Triple("105", "Failed to parse request", 400),
                Triple("106", "Internal error 1C", 500),
                Triple("107", "Internal error", 500),
                Triple("108", "Error request", 400),
                Triple("109", "Error XML structure", 400),
                Triple("110", "Disconnected", 500),
                Triple("111", "The request is processed", 500),
                Triple("113", "Any error", 500)
        )
        return testCases.map {
            DynamicTest.dynamicTest(
                    " Test for translation " + " case ${it.first} == ${it.second}",
                    {
                        val message = "sample message"
                        val serviceResponse = createResponse(it.first, message)

                        val response = mapper?.map(serviceResponse.toEither())!!

                        assertResponseIs(response, it.second, message, it.third)
                    })
        }
    }

    private fun createResponse(code: String, message: String): Response {
        val serviceResponse = Response()
        val message = message
        serviceResponse.errors = ResponseFactory.error(code, message)
        return serviceResponse
    }

    private fun assertResponseIs(response: Either<List<Error>, Results>, first: String, internal: String, code: Int) {
        val expected = Either.left(listOf(BusinessError.aNew(first, internal, code)))
        assertThat(response).isEqualTo(expected)
    }

}