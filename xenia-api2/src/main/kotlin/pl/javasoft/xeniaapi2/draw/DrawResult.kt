package pl.javasoft.xeniaapi2.draw

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import pl.javasoft.xeniaapi2.events.Event
import pl.javasoft.xeniaapi2.members.Member
import pl.javasoft.xeniaapi2.prizes.Prize
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
class DrawResult {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long?=null

    @ManyToOne
    var giveAway:GiveAway?=null

    @ManyToOne
    var member:Member?=null

    constructor(giveAway: GiveAway, member: Member){
        this.giveAway=giveAway
        this.member=member
    }

    override fun toString(): String {
        return "DrawResult(id=$id, giveAway=$giveAway, member=$member)"
    }


}

interface DrawResultRepository: JpaRepository<DrawResult, Long>{
    fun countAllByGiveAway(giveAway: GiveAway):Long

    @Query("SELECT dr FROM DrawResult dr WHERE dr.giveAway.event.id = :event")
    fun findAllByEvent(@Param("event") event: Long):List<DrawResult>

    @Query("SELECT dr FROM DrawResult dr WHERE dr.giveAway.prize = :prize")
    fun findAllByPrize(@Param("prize") prize: Prize):List<DrawResult>
}
