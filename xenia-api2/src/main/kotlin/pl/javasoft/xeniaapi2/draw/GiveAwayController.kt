package pl.javasoft.xeniaapi2.draw

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.javasoft.xeniaapi2.prizes.PrizeRepository

@RequestMapping(value = ["/events/{id}/giveaways"], produces = [MediaType.APPLICATION_JSON_VALUE])
@RestController
class GiveAwayController(private val giveAwayRepository: GiveAwayRepository,private val prizeRepository: PrizeRepository, private val drawResultRepository: DrawResultRepository) {

    @GetMapping
    fun listAll(@PathVariable("id") eventId: Long) = giveAwayRepository.findAllByEventId(eventId)

    @GetMapping(value = ["/prizes/queue"])
    fun prizesQueue(@PathVariable("id") eventId: Long): MutableList<GiveAway> {
        var giveAways = mutableListOf(*giveAwayRepository.findAllByEventId(eventId).toTypedArray())
        val drawResults = drawResultRepository.findAllByEvent(eventId)

        giveAways = giveAways.flatMap { g-> List(g.amount){g} }.toMutableList()
        drawResults.map { it.giveAway }.forEach {
            val index = giveAways.indexOf(it)
            if(index>0){
                giveAways.removeAt(index)
            }
        }

        return giveAways
    }

}
