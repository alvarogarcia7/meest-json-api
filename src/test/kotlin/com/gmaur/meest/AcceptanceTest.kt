package com.gmaur.meest

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class AcceptanceTest {
    @Test
    fun `query by city`() {
        assertThat(firstCity(queryByCity("Львов"))).isEqualTo("Львов")
    }

    private fun queryByCity(s: String): List<Result> {
        return Meest(configuration()).byCity(s)
    }

    private fun configuration(): Meest.Configuration {
        return Meest.Configuration(TODO("insert credentials"), TODO("insert credentials"), "http://api1c.meest-group.com/services/1C_Query.php")
    }

    private fun firstCity(collection: Iterable<Result>): String {
        return collection.first().city
    }

}