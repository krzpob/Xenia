package pl.javasoft.xeniaapi2.events

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import org.joda.time.DateTime
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@CrossOrigin(origins = ["https://xenia-agile-ng.herokuapp.com","http://localhost:8080"])
@RestController
@RequestMapping(value = ["/events"], produces = ["application/json"])
class EventController (private val eventRepository: EventRepository){

    val log: Logger = LoggerFactory.getLogger(EventController::class.java)

    @GetMapping
    fun listAll():List<Event> = eventRepository.findAll()

    @GetMapping(value = ["{id}"])
    fun eventDetails(@PathVariable id:Long):Event = eventRepository.getOne(id)

    @PostMapping
    fun create(@RequestBody event: Event):Event = eventRepository.save(event)

    @PostMapping(value = ["/import"], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun import(@RequestParam("file")file: MultipartFile):List<Event>{
        val reader = csvReader { charset="UTF-8" }
        val eventList = mutableListOf<Event>()
        reader.open(file.resource.inputStream){
            readAllAsSequence().forEach {
                log.info("Load row: ${it[0]},${it[1]},${DateTime.parse(it[1])}")
                eventList.add(Event.create(it[0], DateTime.parse(it[1])))
            }
        }


        return eventRepository.saveAll(eventList)

    }

}
