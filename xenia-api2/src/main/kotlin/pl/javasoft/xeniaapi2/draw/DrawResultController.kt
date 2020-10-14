package pl.javasoft.xeniaapi2.draw

import org.apache.commons.io.IOUtils
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.ByteArrayInputStream
import java.nio.charset.Charset
import javax.servlet.http.HttpServletResponse

@CrossOrigin(origins = ["https://xenia-agile-ng.herokuapp.com","http://localhost:8080"])
@RequestMapping(value = ["/events/{id}/giveaways/result"], produces = [MediaType.APPLICATION_JSON_VALUE])
@RestController
class DrawResultController(private val drawResultRepository: DrawResultRepository) {

    @GetMapping
    fun listDrawResults(@PathVariable("id") eventId:Long)=
        drawResultRepository.findAllByEvent(eventId)

    @GetMapping(value = ["/grouped"])
    fun grouped(@PathVariable("id") eventId:Long):Map<Long?,List<String?>> {
        return drawResultRepository.findAllByEvent(eventId).groupBy { it.giveAway!!.id }
                .mapValues { it.value.map { it1->it1.member!!.name } }
    }

    @GetMapping(value = ["/csv"], produces = [MediaType.APPLICATION_OCTET_STREAM_VALUE])
    fun downloadCsv(@PathVariable("id")eventId: Long, httpServletResponse: HttpServletResponse){
        val drawResult = drawResultRepository.findAllByEvent(eventId)
        var csv = "\"Member ID\",\"Won prize\",\"Member name\",\"Member profile\"\n"
        csv+=drawResult.map { String.format("%d,\"%s\",\"%s\",\"%s\"",
                        it.member?.id,
                        it.giveAway?.prize?.name?.replace(oldChar = '"', newChar = '\''),
                        it.member?.name?.replace(oldChar = '"', newChar = '\''),
                        it.member?.profileUrl?.replace(oldChar = '"', newChar = '\'')?:""
                    ) }.joinToString("\n")
        val fileName="Meetup-${eventId}-draw-results.csv"
        httpServletResponse.contentType="text/csv"
        httpServletResponse.setHeader("Content-Disposition","attachment; filename=${fileName}")
        IOUtils.copy(ByteArrayInputStream(csv.toByteArray(Charset.forName("UTF-8"))),httpServletResponse.outputStream)

    }
}
