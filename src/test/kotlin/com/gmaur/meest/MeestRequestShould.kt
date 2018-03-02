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
        val request = MeestR.parse(mapOf(field to value))

        assertThat(request.isRight()).isTrue()
        assertThat(request.map {
            val softly = SoftAssertions()
            softly.assertThat(it.where).isEqualTo("$field='$value'")
            softly.assertAll()
        })
    }

    @Test
    fun `join multiple criterias by OR`() {
        val field1 = "CityDescriptionRU"
        val value1 = "Lvov"
        val field2 = "CityDescriptionUA"
        val value2 = "Lviv"
        val values = mapOf(
                field1 to value1,
                field2 to value2)
        val request = MeestR.parse(values)

        assertThat(request.isRight()).isTrue()
        assertThat(request.map {
            val softly = SoftAssertions()
            softly.assertThat(it.where).isEqualTo("$field1='$value1' OR $field2='$value2'")
            softly.assertAll()
        })
    }

    @Test
    fun `requires a criteria`() {
        val values = mapOf<String, String>()
        val request = MeestR.parse(values)

        assertThat(request.isLeft()).isTrue()
        assertThat(request.mapLeft {
            val softly = SoftAssertions()
            softly.assertThat(it.first()).isEqualTo(BusinessError.aNew("Need a criteria to filter on", "Received none", 400))
            softly.assertAll()
        })
    }
}