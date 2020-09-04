package pl.javasoft.xeniaapi2.events

import org.hibernate.annotations.ColumnDefault
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import pl.javasoft.xeniaapi2.members.Member
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
class Attendee(){

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: AttendeeId?=null

    @ColumnDefault(value = "false")
    var absent: Boolean=false

    constructor(event: Event, member: Member):this(){
        id = AttendeeId(event, member)
    }

    fun getEvent():Event? = id?.event

    fun getMember():Member?=id?.member

    override fun toString(): String {
        return "Attendee(id=$id)"
    }


}

@Embeddable
data class AttendeeId(
    @ManyToOne
    val event: Event,
    @ManyToOne
    val member: Member

): Serializable


interface AttendeeRepository: JpaRepository<Attendee, AttendeeId>{
    @Query("SELECT a FROM Attendee a WHERE a.id.event.id=:eventId")
    fun findAllByEventId(@Param("eventId")eventId: Long):List<Attendee>

    @Query("SELECT a FROM Attendee a WHERE a.id.member.id=:memberId")
    fun findAllByMemberId(@Param("memberId")memberId: Long):List<Attendee>

    @Query("SELECT a FROM Attendee a WHERE a.id.event.id = :eventId AND a.absent = false")
    fun findAllPresentMembersAtTheEvent(@Param("eventId")eventId: Long):List<Attendee>
}
