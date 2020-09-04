package pl.javasoft.xeniaapi2.filter

import org.springframework.stereotype.Component
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.FilterConfig
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletResponse

@Component
class AllowedMethodsFilter: Filter{
    override fun doFilter(req: ServletRequest?, res: ServletResponse?, chain: FilterChain?) {
        val response = res as HttpServletResponse
        response.setHeader("Access-Control-Allow-Origin", "*")
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE, PATCH")
        response.setHeader("Access-Control-Max-Age", "3600")
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with, Content-Type, Accept")
        chain?.doFilter(req, response)
    }

    override fun init(filterConfig: FilterConfig?) {}

    override fun destroy() {}

}
