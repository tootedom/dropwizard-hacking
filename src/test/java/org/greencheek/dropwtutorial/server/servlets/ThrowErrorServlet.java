package org.greencheek.dropwtutorial.server.servlets;

import org.apache.commons.io.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.zip.GZIPInputStream;

/**
 * Created by dominictootell on 16/08/2014.
 */
public class ThrowErrorServlet extends HttpServlet {

    public static final String HEADER = "X-Status";
    public static final String VALUE = "500";
    public static final String ERROR_MSG = "Contract an Administrator";

    private final int errorCode;
    private final int numberOfErrorsBeforeOk;
    private final String okContent;
    private final int okStatusCode;

    private final AtomicLong requests = new AtomicLong(0);

    public final List<String> recievedEntities;

    public ThrowErrorServlet(int errorCode,int numberOfErrorsBeforeOk,
                             int okStatusCode, String okContent) {
        this.errorCode = errorCode;
        this.numberOfErrorsBeforeOk = numberOfErrorsBeforeOk;
        this.okContent = okContent;
        this.okStatusCode = okStatusCode;
        this.recievedEntities = new CopyOnWriteArrayList();
    }

    public List<String> getRecievedEntities() {
        return recievedEntities;
    }

    protected void service( HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        long requestNumber = requests.incrementAndGet();
        long now = System.currentTimeMillis();


        StringWriter swriter = new StringWriter();
        String headerValue = request.getHeader("content-encoding");


        if(headerValue == null) {
            IOUtils.copy(request.getInputStream(), swriter, "UTF-8");
        }
        else if(headerValue.equalsIgnoreCase("gzip")) {
            IOUtils.copy(new GZIPInputStream(request.getInputStream()), swriter);
        }
        else {
            IOUtils.copy(request.getInputStream(), swriter, "UTF-8");
        }

        recievedEntities.add(swriter.toString());

        PrintWriter writer = response.getWriter();
        if(requestNumber <= numberOfErrorsBeforeOk) {
            response.sendError(errorCode, ERROR_MSG);
        } else {
            response.setStatus(okStatusCode);
            writer.write(okContent);
            writer.flush();
        }


    }
}