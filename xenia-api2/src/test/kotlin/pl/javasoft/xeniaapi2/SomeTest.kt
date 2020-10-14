package pl.javasoft.xeniaapi2

import org.junit.jupiter.api.Test

class SomeTest {
    @Test
    fun parseUrl(){
        val dbUrl = "postgres://ninffufgsucyxh:804e8f8ff5d70cae9897ef80ff0d4ffa820aa2c988362f1a078e002d096a1ff7@ec2-54-170-123-247.eu-west-1.compute.amazonaws.com:5432/d87qp2a8ok89se"
        val regex = Regex("postgres:\\/\\/([a-z]+):([a-z0-9]+)@(.+)")
        val lastIndex=dbUrl.lastIndexOf(':')
        val username = dbUrl.subSequence(10,lastIndex)
        println(regex.find(dbUrl)?.groupValues)
    }
}
