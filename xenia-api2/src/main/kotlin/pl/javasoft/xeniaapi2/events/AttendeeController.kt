package pl.javasoft.xeniaapi2.events

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping(value = ["/events/{id}/attendees"], produces = [MediaType.APPLICATION_JSON_VALUE] )
class AttendeeController(
        private val attendeeRepository: AttendeeRepository,
        private val attendeeService: AttendeeService
) {
    @GetMapping
    fun listAll(@PathVariable id: Long):List<Attendee> = attendeeRepository.findAllByEventId(id)

    @PostMapping("/import")
    fun import(@PathVariable id: Long,@RequestParam("file") file: MultipartFile):List<Attendee>{
        val reader = csvReader{
            charset="UTF-8"
            skipEmptyLine =true
            delimiter=';'
        }
        var attendeeList = emptyList<Attendee>()
        reader.open(file.resource.inputStream){
            attendeeList = attendeeService.import(id,readAllAsSequence())
        }
        return attendeeList
    }

    @PostMapping(value = [],consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun create(@PathVariable id: Long,@RequestBody attendeeRequestCreate: AttendeeRequestCreate):List<Attendee>{
        attendeeService.create(id,attendeeRequestCreate)
        return attendeeRepository.findAllByEventId(eventId = id)
    }
}

data class AttendeeRequestCreate(val name: String, val profileUrl: String?, val email: String?)
