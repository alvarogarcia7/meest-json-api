package com.gmaur.meest.requests

import arrow.core.Either
import com.gmaur.meest.BusinessError
import com.gmaur.meest.MeestR
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.Test
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import kotlin.reflect.KFunction1

@RunWith(JUnitPlatform::class)
abstract class MeestRequestShould {
    abstract fun sut(): KFunction1<Map<String, String>, Either<List<BusinessError>, MeestR>>

    @Test
    fun `parse by a single criteria`() {
        val field = "CityDescriptionRU"
        val value = "Lvov"
        val request = sut()(mapOf(field to value))

        assertThat(request.isRight()).isTrue()
        assertThat(request.map {
            val softly = SoftAssertions()
            softly.assertThat(it.where).isEqualTo("$field='$value'")
            softly.assertAll()
        })
    }

    @Test
    fun `requires a criteria`() {
        val values = mapOf<String, String>()
        val request = sut()(values)

        assertThat(request.isLeft()).isTrue()
        assertThat(request.mapLeft {
            val softly = SoftAssertions()
            softly.assertThat(it.first()).isEqualTo(BusinessError.aNew("Need a criteria to filter on", "Received none", 400))
            softly.assertAll()
        })
    }
}

