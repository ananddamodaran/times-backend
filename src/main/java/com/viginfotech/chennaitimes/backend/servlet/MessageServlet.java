package com.viginfotech.chennaitimes.backend.servlet;


import com.viginfotech.chennaitimes.backend.apis.gcm.MessagingEndpoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by anand on 3/8/16.
 */
public class MessageServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
        MessagingEndpoint endpoint = new MessagingEndpoint();
        endpoint.sendMessage("gcm");
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
