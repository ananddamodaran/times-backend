package com.viginfotech.chennaitimes.backend.servlet


import com.viginfotech.chennaitimes.backend.apis.gcm.MessagingEndpoint
import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by anand on 3/8/16.
 */
class MessageServlet : HttpServlet() {
    @Throws(ServletException::class, IOException::class)
    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        super.doGet(req, resp)
        val endpoint = MessagingEndpoint()
        endpoint.sendMessage("gcm")
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT)
    }
}
