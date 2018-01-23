package com.gmaur.meest

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class AcceptanceTest {
    @Test
    fun `query by city`() {
        assertThat(firstCity(queryByCity("Львов"))).isEqualTo("Львов")

    }

    private fun queryByCity(s: String): List<Result> {
        return Meest().byCity(s)
    }

    private fun firstCity(collection: Iterable<Result>): String {
        return collection.first().city
    }

}