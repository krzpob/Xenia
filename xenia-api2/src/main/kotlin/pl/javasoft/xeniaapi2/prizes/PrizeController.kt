package pl.javasoft.xeniaapi2.prizes

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.websocket.server.PathParam

@RestController
@RequestMapping(produces = [MediaType.APPLICATION_JSON_VALUE], value = ["/prizes"])
class PrizeController(private val prizeRepository: PrizeRepository) {

    @GetMapping
    fun listAll(): List<Prize> = prizeRepository.findAll()

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun create(@RequestBody prize: Prize): Prize = prizeRepository.save(prize)

    @GetMapping(value = ["/active"])
    fun listActive(): List<Prize> = prizeRepository.findAllByInactiveOrderByIdAsc(false)

    @GetMapping(value = ["/inactive"])
    fun listInActive(): List<Prize> = prizeRepository.findAllByInactiveOrderByIdAsc(true)

    @PutMapping(value = ["{id}"])
    fun update(@RequestBody prize: Prize, @PathVariable id: Long): Prize {
        assert(prizeRepository.existsById(id)) {
            "Prize with id $id does not exist"
        }
        prize.id=id
        return prizeRepository.save(prize)
    }

    @RequestMapping(value = ["/{id}/activate"])
    fun activate(@PathVariable id: Long):Prize{
        assert(prizeRepository.existsById(id)) {
            "Prize with id $id does not found"
        }

        val prize = prizeRepository.findById(id).get()
        prize.inactive=false
        return prizeRepository.save(prize)
    }

    @RequestMapping(value = ["/{id}/disable"])
    fun disable(@PathVariable("id") id:Long):Prize{
        assert(prizeRepository.existsById(id)) {
            "Prize with id $id does not found"
        }
        val prize = prizeRepository.findById(id).get()
        prize.inactive=true
        return prizeRepository.save(prize)
    }
}
