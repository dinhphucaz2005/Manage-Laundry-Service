package manage.laundry.service.configuration

import jakarta.servlet.ReadListener
import jakarta.servlet.ServletInputStream
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletRequestWrapper
import java.io.*

class MultiReadHttpServletRequest(request: HttpServletRequest) : HttpServletRequestWrapper(request) {
    private val requestBody: ByteArray = request.inputStream.readBytes()

    fun getRequestBody(): String = String(requestBody)

    override fun getInputStream(): ServletInputStream {
        val byteArrayInputStream = ByteArrayInputStream(requestBody)
        return object : ServletInputStream() {
            override fun isFinished(): Boolean = byteArrayInputStream.available() == 0
            override fun isReady(): Boolean = true
            override fun setReadListener(readListener: ReadListener?) {
                throw UnsupportedOperationException()
            }
            override fun read(): Int = byteArrayInputStream.read()
        }
    }

    override fun getReader(): BufferedReader = BufferedReader(InputStreamReader(inputStream))
}
