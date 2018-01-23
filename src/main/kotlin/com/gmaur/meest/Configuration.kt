package com.gmaur.meest

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.env.PropertiesPropertySourceLoader
import org.springframework.context.annotation.PropertySource
import org.springframework.core.io.support.DefaultPropertySourceFactory
import org.springframework.core.io.support.EncodedResource
import org.springframework.stereotype.Component

// Source: https://stackoverflow.com/questions/45880494/configurationproperties-loading-list-from-yml#45882088

@Component
@ConfigurationProperties
@PropertySource("classpath:/private.properties", "classpath:/application.properties", factory = PropertiesPropertyLoaderFactory::class)
class Configuration {
    var username: String = ""
    var password: String = ""
    var url: String = ""
}

open class PropertiesPropertyLoaderFactory : DefaultPropertySourceFactory() {
    override fun createPropertySource(name: String?, resource: EncodedResource): org.springframework.core.env.PropertySource<*> {
        return PropertiesPropertySourceLoader().load(resource.resource.filename, resource.resource, null)
    }
}