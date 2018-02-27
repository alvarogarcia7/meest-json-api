package com.gmaur.meest

import arrow.core.Either
import org.assertj.core.api.Assertions
import org.junit.Ignore
import org.junit.Test

class MapperShould {
    @Ignore("current feature")
    @Test
    fun `do not parse results when there are errors`() {
        val response = Response()
        val mapper = Meest.Mapper()
        Assertions.assertThat(mapper.map(response)).isEqualTo(Either.left(listOf("001 - Authentication error")))
    }
}