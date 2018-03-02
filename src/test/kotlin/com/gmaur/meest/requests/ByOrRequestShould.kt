package com.gmaur.meest.requests

import arrow.core.Either
import com.gmaur.meest.BusinessError
import com.gmaur.meest.MeestR
import org.assertj.core.api.Assertions
import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.Test
import kotlin.reflect.KFunction1

class ByOrRequestShould : MeestRequestShould() {
    override fun sut(): KFunction1<Map<String, String>, Either<List<BusinessError>, MeestR>> {
        return MeestR.Companion::parseMultipleByOr
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
        val request = sut()(values)

        Assertions.assertThat(request.isRight()).isTrue()
        Assertions.assertThat(request.map {
            val softly = SoftAssertions()
            softly.assertThat(it.where).isEqualTo("$field1='$value1' OR $field2='$value2'")
            softly.assertAll()
        })
    }

}