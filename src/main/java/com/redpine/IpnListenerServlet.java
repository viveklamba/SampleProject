package com.redpine;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * Servlet implementation class IpnListenerServlet
 */

@WebServlet("/ipn-listener")
public class IpnListenerServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(IpnListenerServlet.class);

    public IpnListenerServlet() {
        super();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final IpnHandler handlerObj = new IpnHandler();
        LOGGER.info("Inside doGet method ");
        try {
            handlerObj.handleIpn(request);
        } catch (final IpnException e) {
            LOGGER.error("Error while receiving data from paypal ", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
