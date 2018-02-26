package com.gmaur.meest

import org.assertj.core.api.Assertions
import org.junit.Test

class ResponseShould {

    @Test
    fun `a response with code 000 is a success`() {
        val serviceResponse = Response()
        serviceResponse.errors = ResponseFactory.error("000", "")

        val response = serviceResponse.toEither()

        Assertions.assertThat(response.isRight()).isTrue()
    }
}