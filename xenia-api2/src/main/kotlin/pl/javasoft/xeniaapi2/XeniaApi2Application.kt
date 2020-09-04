package pl.javasoft.xeniaapi2

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.Module
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.joda.time.DateTime
import org.slf4j.Logger
import org.slf4j.LoggerFactory
//import org.joda.time.DateTime
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary

@SpringBootApplication
class XeniaApi2Application{

    val log:Logger = LoggerFactory.getLogger(XeniaApi2Application::class.java)

    @Bean
    fun openApi():OpenAPI{
        return OpenAPI().info(Info().title("xenia api 2").version("1.0"))
    }
}

fun main(args: Array<String>) {
    runApplication<XeniaApi2Application>(*args)
}

