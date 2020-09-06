package pl.javasoft.xeniaapi2.members

import org.springframework.data.jpa.repository.JpaRepository
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Member{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long?=null

    var name: String?=null
    var email: String?=null
    var profileUrl: String?=null
}

interface MemberRepository: JpaRepository<Member, Long>{
    fun findByName(name: String): Member
}
