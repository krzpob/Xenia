package pl.javasoft.xeniaapi2.draw

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import pl.javasoft.xeniaapi2.events.Event
import pl.javasoft.xeniaapi2.prizes.Prize
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
class GiveAway {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long?=null

    @ManyToOne
    var prize: Prize?=null

    @JsonIgnore
    @ManyToOne
    var event:Event?=null

    var amount: Int=0

    var emailRequired: Boolean=false

    constructor(prize: Prize, event: Event, amount: Int,emailRequired: Boolean){
        this.prize=prize
        this.event=event
        this.amount=amount
        this.emailRequired=emailRequired
    }

}

interface GiveAwayRepository: JpaRepository<GiveAway, Long>{

    fun findAllByEventId(eventId: Long):List<GiveAway>

    fun findByPrizeIdAndEventId(prizeId: Long, eventId: Long):List<GiveAway>
}
