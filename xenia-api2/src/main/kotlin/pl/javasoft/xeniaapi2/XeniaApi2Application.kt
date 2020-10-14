package pl.javasoft.xeniaapi2

//import org.joda.time.DateTime
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import javax.sql.DataSource


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

@Profile("heroku")
@Configuration
class HerokuConfig {

    val log:Logger = LoggerFactory.getLogger(HerokuConfig::class.java)

    @Bean
    fun dataSource(@Value("\${spring.datasource.hikari.jdbc-url}") urlDb: String):DataSource{
        val config = HikariConfig()
        val regex = Regex("postgres:\\/\\/([a-z]+):([a-z0-9]+)@(.+)")
        val groups = regex.find(urlDb)?.groupValues

        config.jdbcUrl="jdbc:postgresql://${groups?.get(3)}?ssl=true&sslmode=require"
        config.username=groups?.get(1)
        config.password=groups?.get(2)
        return HikariDataSource(config)
    }
}
