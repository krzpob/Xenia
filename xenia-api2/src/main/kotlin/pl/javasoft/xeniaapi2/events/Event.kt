package pl.javasoft.xeniaapi2.events

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import org.hibernate.annotations.Type
import org.joda.time.DateTime
import org.springframework.data.jpa.repository.JpaRepository
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long?=null

    var name: String?=null

    @JsonFormat(pattern = "yyyy-mm-dd'T'HH:MM:ss")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    var startDateTime: DateTime?=null

    var time: String
        set(value) {startDateTime= DateTime.parse(value)}
        get() = startDateTime.toString()

    companion object {
        fun create(name: String, time: DateTime):Event{
            val event = Event()
            event.name=name
            event.startDateTime=time
            return event
        }
    }

}

interface EventRepository: JpaRepository<Event, Long>
