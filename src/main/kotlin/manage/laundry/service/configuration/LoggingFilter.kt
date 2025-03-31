package manage.laundry.service.configuration

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.io.IOException

@Component
class LoggingFilter : Filter {
    private val logger = LoggerFactory.getLogger(LoggingFilter::class.java)

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        if (request is HttpServletRequest) {
            val wrappedRequest = MultiReadHttpServletRequest(request)

            val requestLog = buildString {
                appendLine("\n==================== 🌐 NEW REQUEST ====================")
                appendLine("📢 Request to: ${request.method} ${request.requestURI}")
                appendLine("📝 Query Params: ${request.queryString ?: "{}"}")

                val body = wrappedRequest.getRequestBody()
                if (body.isNotEmpty()) {
                    appendLine("📦 Request Body: $body")
                }

                append("========================================================")
            }

            logger.info(requestLog) // Log tất cả thông tin trong 1 dòng duy nhất

            chain.doFilter(wrappedRequest, response)
        } else {
            chain.doFilter(request, response)
        }
    }
}
