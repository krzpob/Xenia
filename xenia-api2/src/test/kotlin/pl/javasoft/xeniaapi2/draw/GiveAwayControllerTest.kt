package pl.javasoft.xeniaapi2.draw

import org.joda.time.DateTime
import org.junit.jupiter.api.Test

internal class GiveAwayControllerTest {

    @Test
    fun test() {
        val str = "2020-09-23T18:00:00.0"
        println("date: ${DateTime.parse(str)}")

    }
}
