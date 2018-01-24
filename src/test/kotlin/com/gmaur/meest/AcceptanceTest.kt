package com.gmaur.meest

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

@RunWith(SpringJUnit4ClassRunner::class)
//@ContextConfiguration(classes = arrayOf(AcceptanceTest.SpringConfig::class))
@EnableAutoConfiguration
class AcceptanceTest {

    @Autowired
    private var configuration: Configuration = Configuration()

    @Test
    fun `query by city`() {
        assertThat((queryByCity("Львов").first().city)).isEqualTo("Львов")
    }

    private fun queryByCity(s: String): Results {
        return Meest(configuration, Meest.Mapper()).byCity(s)
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