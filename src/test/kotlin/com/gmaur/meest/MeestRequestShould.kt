package com.gmaur.meest

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.Test
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
class MeestRequestShould {
    @Test
    fun `parse by a single criteria`() {
        val field = "CityDescriptionRU"
        val value = "Lvov"
        val request = MeestRequest.parse(mapOf(field to value))

        assertThat(request.isRight()).isTrue()
        assertThat(request.map {
            val softly = SoftAssertions()
            softly.assertThat(it.getWhere()).isEqualTo("$field='$value'")
            softly.assertAll()
        })
    }

    @Test
    fun `parse by only the first criteria`() {
        val field1 = "CityDescriptionRU"
        val value1 = "Lvov"
        val field2 = "CityDescriptionUA"
        val value2 = "Lviv"
        val values = mapOf(
                field1 to value1,
                field2 to value2)
        val request = MeestRequest.parse(values)

        assertThat(request.isLeft()).isTrue()
        assertThat(request.mapLeft {
            val softly = SoftAssertions()
            softly.assertThat(it.first()).isEqualTo(BusinessError.aNew("Cannot parse two criterias at the same time", "Received: $field1=$value1, $field2=$value2", 400))
            softly.assertAll()
        })
    }

    @Test
    fun `requires a criteria`() {
        val values = mapOf<String, String>()
        val request = MeestRequest.parse(values)

        assertThat(request.isLeft()).isTrue()
        assertThat(request.mapLeft {
            val softly = SoftAssertions()
            softly.assertThat(it.first()).isEqualTo(BusinessError.aNew("Need a criteria to filter on", "Received none", 400))
            softly.assertAll()
        })
    }
}