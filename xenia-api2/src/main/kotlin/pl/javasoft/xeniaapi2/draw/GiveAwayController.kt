package pl.javasoft.xeniaapi2.draw

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.javasoft.xeniaapi2.events.Event
import pl.javasoft.xeniaapi2.events.EventRepository
import pl.javasoft.xeniaapi2.prizes.PrizeRepository
import javax.validation.Valid
import javax.validation.constraints.Max
import javax.validation.constraints.Min

@CrossOrigin(origins = ["https://xenia-agile-ng.herokuapp.com","http://localhost:8080"])
@RequestMapping(value = ["/events/{id}/giveaways"], produces = [MediaType.APPLICATION_JSON_VALUE])
@RestController
class GiveAwayController(private val giveAwayRepository: GiveAwayRepository,private val prizeRepository: PrizeRepository, private val drawResultRepository: DrawResultRepository,private val eventRepository: EventRepository) {

    val log:Logger = LoggerFactory.getLogger(GiveAwayController::class.java)

    @GetMapping
    fun listAll(@PathVariable("id") eventId: Long) = giveAwayRepository.findAllByEventId(eventId)

    @GetMapping(value = ["/prizes/queue"])
    fun prizesQueue(@PathVariable("id") eventId: Long): MutableList<GiveAway> {
        var giveAways = mutableListOf(*giveAwayRepository.findAllByEventId(eventId).toTypedArray())
        val drawResults = drawResultRepository.findAllByEvent(eventId)
        log.info("Podarunki: {}", giveAways)
        log.info("Wygrani: {}", drawResults)
        giveAways = giveAways.flatMap { g-> List(g.amount){g} }.toMutableList()
        log.info("Podarunki2: {}", giveAways)
        drawResults.map { it.giveAway }.forEach {
            val index = giveAways.indexOf(it)
            log.info("Index: {}", index)
            log.info("Poadrek wygrany: {}", it)
            if(index>=0){
                giveAways.removeAt(index)
            }
        }
        log.info("Podarunki3: {}", giveAways)
        return giveAways
    }

    @PostMapping()
    fun create(@PathVariable("id") eventId: Long,  @Valid @RequestBody createGiveAwayRequest: CreateGiveAwayRequest):GiveAway{
        val event:Event = eventRepository.findById(eventId).orElseThrow()

        val giveAway=GiveAway(
                        prize = prizeRepository.getOne(createGiveAwayRequest.prize),
                        amount = createGiveAwayRequest.amount,
                        event = event,
                        emailRequired = createGiveAwayRequest.emailRequired
        )

        return giveAwayRepository.save(giveAway)
    }

    @PutMapping("/{giveaway}")
    fun update(@PathVariable("id")eventId: Long, @PathVariable("giveaway") giveAwayId: Long,@Valid @RequestBody updateGiveAwayRequest: UpdateGiveAwayRequest):GiveAway{

        val giveAway = giveAwayRepository.getOne(giveAwayId)
        giveAway.amount=updateGiveAwayRequest.amount
        giveAway.emailRequired=updateGiveAwayRequest.emailRequired

        return giveAwayRepository.save(giveAway)

    }

    @DeleteMapping("/{giveaway}")
    fun delete(@PathVariable("giveaway")giveAwayId: Long){
        giveAwayRepository.deleteById(giveAwayId)
    }

}


data class CreateGiveAwayRequest(@Max(1L)val prize: Long, @Min(0L) val amount: Int, val emailRequired: Boolean)

data class UpdateGiveAwayRequest(@Min(0L) val amount: Int, val emailRequired: Boolean)
