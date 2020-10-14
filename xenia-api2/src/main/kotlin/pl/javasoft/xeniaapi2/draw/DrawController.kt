package pl.javasoft.xeniaapi2.draw

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import pl.javasoft.xeniaapi2.events.EventRepository
import pl.javasoft.xeniaapi2.members.MemberRepository

@CrossOrigin(origins = ["https://xenia-agile-ng.herokuapp.com","http://localhost:8080"])
@RestController
@RequestMapping(value = ["/events/{id}/giveaways/{giveAway}/draw"], produces = [MediaType.APPLICATION_JSON_VALUE])
class DrawController(private val drawService: DrawService,
                     private val memberRepository: MemberRepository,
                     private val eventRepository: EventRepository,
                     private val giveAwayRepository: GiveAwayRepository
){

    @GetMapping
    fun drawWinnerCandidate(@PathVariable("id")eventId:Long,
                            @PathVariable("giveAway")giveAwayId: Long,
                            @RequestParam(value = "absent", required = false) absentMember:Long?,
                            @RequestParam(value = "skipped", required = false) skippedMember:Long?
    ):DrawResult{
        if(absentMember!=null){
            drawService.markMemberAsAbsentForCurrentDraw(memberRepository.getOne(absentMember), eventRepository.getOne(eventId))
        } else if(skippedMember!=null){
            drawService.setGiveAwaySkippedForMember(memberRepository.getOne(skippedMember), giveAwayRepository.getOne(giveAwayId))
        }
        return drawService.drawWinnerCandidate(giveAwayRepository.getOne(giveAwayId))
    }

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun confirmWinner(@PathVariable("giveAway") giveAwayId: Long,@RequestBody confirmWinnerRequest: ConfirmWinnerRequest){
        drawService.confirmWinner(giveAwayRepository.getOne(giveAwayId), confirmWinnerRequest.memberId)
    }

}

data class ConfirmWinnerRequest(val memberId: Long)
