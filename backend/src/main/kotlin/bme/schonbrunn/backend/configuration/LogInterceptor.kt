package bme.schonbrunn.backend.configuration

import bme.schonbrunn.backend.user.repository.UserRepository
import org.apache.logging.log4j.ThreadContext
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class LogInterceptor(private val userRepository: UserRepository) : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val executionStartTime = System.currentTimeMillis()
        request.setAttribute("start-time", executionStartTime)
        val operationPath = request.servletPath
        val principal = request.userPrincipal
        val user = principal?.name?.let { userRepository.findByEmail(principal.name) }
        ThreadContext.put("path", operationPath)
        ThreadContext.put("user", user?.id?.toString() ?: "Anonymous")

        return true
    }

    override fun afterCompletion(
        request: HttpServletRequest, response: HttpServletResponse, handler: Any,
        exception: Exception?
    ) {
        ThreadContext.clearMap()
    }
}