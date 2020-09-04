package pl.javasoft.xeniaapi2.prizes

import org.hibernate.annotations.ColumnDefault
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Prize {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long?=null

    var name: String?=null

    var imageUrl: String?=null

    @ColumnDefault("false")
    var inactive: Boolean=false
}

interface PrizeRepository: JpaRepository<Prize, Long>{
    fun findByName(name: String): Prize
    fun findAllByInactiveOrderByIdAsc(inactive: Boolean):List<Prize>
}

