package com.gmaur.meest

import arrow.core.Either
import arrow.core.flatMap
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.fuel.httpGet
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.context.annotation.ComponentScan
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner

//@ContextConfiguration(classes = arrayOf(AcceptanceTest.SpringConfig::class))
@EnableAutoConfiguration
@RunWith(SpringRunner::class)
@ContextConfiguration(classes = [DemoApplication::class])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AcceptanceFeature {

    @LocalServerPort
    var port: Int? = null

    @Autowired
    private var configuration: Configuration = Configuration()

    @Before
    fun setUp() {
        FuelManager.instance.basePath = "http://localhost:" + port!!
    }

    @Test
    fun `query by city`() {
        assertThat(queryByCity(Pair("CityDescriptionRU", "Львов")).first().city.valueRU).isEqualTo("Львов")
    }

    @Test
    fun `query by city criteria`() {
        assertThat(queryByCity(Pair("CityDescriptionRU", "Львов")).first().city.valueRU).isEqualTo("Львов")
    }

    @Test
    fun `query by criteria through the rest api`() {

        val response = droppoints(Pair("CityDescriptionRU", "Львов"))

        assertThat(response.isRight())
        response.bimap(
                {
                    fail("expected a right")
                },
                { (response, result) ->
                    val get = result.get()
                    assertThat(response.statusCode).isEqualTo(200)
                    when (result) {
                        is com.github.kittinunf.result.Result.Success -> {
                            println(result.value)
//                            assertThat(result.value.city).isEqualTo("Львов")
                        }
                        else -> {
                            fail("expected a Result.success")
                        }
                    }
                })
    }

    private fun droppoints(criteria: Pair<String, String>): Either<Exception, Pair<Response, com.github.kittinunf.result.Result<String, FuelError>>> {
        val parameters = listOf(criteria)
        val request = "/droppoints".httpGet(parameters).header("Content-Type" to "application/json")
        try {
            val (_, response, result) = request.responseString()
            return Either.right(Pair(response, result))
        } catch (e: Exception) {
            e.printStackTrace()
            return Either.left(e)
        }

    }

    private fun queryByCity(s: String): Results {
        val get = Meest(Meest.MeestClient(configuration, Meest.ResponseParser()), Meest.Mapper()).request(MeestR.byCity(s))
        println(get)
        return get.get()
    }

    private fun queryByCity(criteria: Pair<String, String>): Results {
        val value = Meest.parse(mapOf(criteria)).flatMap {
            Meest(Meest.MeestClient(configuration, Meest.ResponseParser()), Meest.Mapper()).request(it)
        }
        return value.get()
    }


    @org.springframework.context.annotation.Configuration
    @ComponentScan(basePackageClasses = arrayOf(Configuration::class))
    internal class SpringConfig {
//        @Bean
//        fun propertyConfigInDev(): PropertySourcesPlaceholderConfigurer {
//            return PropertySourcesPlaceholderConfigurer()
//        }
    }


}