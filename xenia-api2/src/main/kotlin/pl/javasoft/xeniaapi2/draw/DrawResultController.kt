package pl.javasoft.xeniaapi2.draw

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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
}
