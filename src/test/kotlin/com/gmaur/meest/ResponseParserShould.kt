package com.gmaur.meest

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.Test
import java.io.FileInputStream
import java.nio.file.Paths

class ResponseParserShould {
    @Test
    fun `parse the XML responses - empty case`() {
        var response = Meest.ResponseParser().read(inputStream("empty1.xml"))

        val result = response.toEither()

        Assertions.assertThat(result.isRight()).isEqualTo(true)
        result.map {
            assertThat(it.resultTable?.items).isNull()
        }
    }

    @Test
    fun `parse the XML responses - valid case`() {
        var response = Meest.ResponseParser().read(inputStream("valid1.xml"))

        val result = response.toEither()


        Assertions.assertThat(result.isRight()).isEqualTo(true)

        result.map {
            val softly = SoftAssertions()
            softly.assertThat(it.resultTable?.items).isNotNull()
            softly.assertThat(it.resultTable?.items).hasSize(64)
            softly.assertThat(it.resultTable?.items?.first()?.UUID).isEqualTo("3158FF42-6D4F-11E7-80C4-1C98EC135263")
            softly.assertThat(it.resultTable?.items?.first()?.BranchCode).isEqualTo("3332")
            softly.assertThat(it.resultTable?.items?.first()?.CityUUID).isEqualTo("62C3D54A-749B-11DF-B112-00215AEE3EBE")
            softly.assertAll()
        }
    }

    private fun inputStream(file: String) = FileInputStream(inTestClasses(file).toFile())


    private fun inTestClasses(file: String) = Paths.get("./target/test-classes/samples/" +
            file)
}