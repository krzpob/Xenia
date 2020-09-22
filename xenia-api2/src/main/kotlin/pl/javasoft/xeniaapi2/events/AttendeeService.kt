package pl.javasoft.xeniaapi2.events

import org.springframework.stereotype.Service
import pl.javasoft.xeniaapi2.members.Member
import pl.javasoft.xeniaapi2.members.MemberRepository

@Service
class AttendeeService(private val memberRepository: MemberRepository,
                      private val eventRepository: EventRepository,
                       private val attendeeRepository: AttendeeRepository
) {

    fun import(id:Long, attendee: Sequence<List<String>>):List<Attendee>{
        attendeeRepository.deleteAll(attendeeRepository.findAllByEventId(id))
        val event = eventRepository.getOne(id)
        val attendeeList = attendee.map {
            var member = Member()
            member.name=it[0]
            member.profileUrl=it[1]
            if(it.size>2) {
                member.email = it[2]
            }
            member = memberRepository.save(member)
            Attendee(event,member)
        }.toList().distinct()
        return attendeeRepository.saveAll(attendeeList)
    }

    fun create(id: Long, attendeeRequestCreate: AttendeeRequestCreate) {
        val member = Member().apply {
            name=attendeeRequestCreate.name
            email=attendeeRequestCreate.email
            profileUrl=attendeeRequestCreate.profileUrl
        }

        val attendee = Attendee(eventRepository.getOne(id),memberRepository.save(member))
        attendeeRepository.saveAndFlush(attendee)

    }
}
