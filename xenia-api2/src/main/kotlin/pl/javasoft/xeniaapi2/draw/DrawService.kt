package pl.javasoft.xeniaapi2.draw

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.Assert
import pl.javasoft.xeniaapi2.events.Attendee
import pl.javasoft.xeniaapi2.events.AttendeeId
import pl.javasoft.xeniaapi2.events.AttendeeRepository
import pl.javasoft.xeniaapi2.events.Event
import pl.javasoft.xeniaapi2.members.Member
import pl.javasoft.xeniaapi2.members.MemberRepository
import java.util.concurrent.ConcurrentSkipListSet
import kotlin.random.Random


@Service
class DrawService(
        private val drawResultRepository: DrawResultRepository,
        private val skippedGiveAwayContainer: SkippedGiveAwayContainer,
        private val attendeeRepository: AttendeeRepository,
        private val memberRepository: MemberRepository
) {

    val log: Logger = LoggerFactory.getLogger(DrawService::class.java)

    @Transactional
    fun drawWinnerCandidate(giveAway: GiveAway): DrawResult{
        log.info("GiveWay: {}", giveAway)
        Assert.state(giveAway.amount> drawResultRepository.countAllByGiveAway(giveAway),"You cannot draw more giveaways for this prize!")
        val attendeeList = attendeeRepository.findAllPresentMembersAtTheEvent(giveAway.event?.id!!)
        Assert.state(attendeeList.isNotEmpty(),"No one attended this event, you cannot draw a giveaway")

        val members= acceptAttendeesWhoDidNotWinAnyPrizeYetDuringTheEventAndDidNotWinThisGiveawayBefore(attendeeList, giveAway)
        log.info("Drawing from {}",members.size)
        val winner = members[Random.nextInt(members.size)]
        return DrawResult(giveAway,winner)
    }

    private fun acceptAttendeesWhoDidNotWinAnyPrizeYetDuringTheEventAndDidNotWinThisGiveawayBefore(attendeeList: List<Attendee>, giveAway: GiveAway): List<Member> {
        val members = attendeeList.map { it.getMember() }.toMutableList()

        val winnersInCurrentEvent = drawResultRepository.findAllByEvent(giveAway.event?.id!!).map { it.member }
        log.info("In this event draw {} members have won already...", winnersInCurrentEvent.size)
        members.removeAll(winnersInCurrentEvent)

        val membersWhoHaveWonThisPrizeAlready = drawResultRepository.findAllByPrize(giveAway.prize!!).mapNotNull { it.member }
        members.removeAll(membersWhoHaveWonThisPrizeAlready)

        val membersWithSkippedGiveAway = skippedGiveAwayContainer.skippedGiveWays[giveAway].orEmpty()
        members.removeAll(membersWithSkippedGiveAway)


        return members.filterNotNull()
    }

    @Transactional
    fun confirmWinner(giveAway: GiveAway, memberId:Long){
        Assert.state(giveAway.amount> drawResultRepository.countAllByGiveAway(giveAway),"You cannot draw more giveaways for this prize!")
        val member= memberRepository.getOne(memberId)
        drawResultRepository.save(DrawResult(giveAway,member))

    }

    @Transactional
    fun markMemberAsAbsentForCurrentDraw(member: Member, event: Event){
        log.info("Member {} is not present at the event, marking as absent to skip this member in next draw...", member.name)

        val attendeeOpt =attendeeRepository.findById(AttendeeId(event, member))
        if(attendeeOpt.isPresent){
            val attendee=attendeeOpt.get()
            attendee.absent=true
            attendeeRepository.save(attendee)
        }


    }

    fun setGiveAwaySkippedForMember(member: Member, giveAway: GiveAway){
        skippedGiveAwayContainer.addSkippedMember(giveAway, member)
    }
}

@Component
class SkippedGiveAwayContainer {

    val skippedGiveWays: MutableMap<GiveAway, MutableSet<Member>> = mutableMapOf()

    fun addSkippedMember(giveAway: GiveAway, member: Member){
        skippedGiveWays.computeIfAbsent(giveAway){
            ConcurrentSkipListSet<Member> { m1, m2 ->  m1.id!!.compareTo(m2.id!!) } }
        }

}



