package pl.javasoft.xeniaapi2.events

import org.springframework.stereotype.Service
import pl.javasoft.xeniaapi2.members.MemberRepository

@Service
class AttendeeService(private val memberRepository: MemberRepository,
                      private val eventRepository: EventRepository,
                      private val attendeeRepository: AttendeeRepository
) {

    fun import(attendee: Sequence<List<String>>):List<Attendee>{
        return attendee.map {
            val event = eventRepository.getOne(it[0].toLong())
            val member = memberRepository.getOne(it[1].toLong())
            Attendee(event,member)
        }.toList()
    }
}
