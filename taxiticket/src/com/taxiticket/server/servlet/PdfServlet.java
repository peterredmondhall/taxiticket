package com.taxiticket.server.servlet;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.taxiticket.server.BookingManager;
import com.taxiticket.server.BookingServiceImpl;
import com.taxiticket.server.ProfileManager;
import com.taxiticket.server.entity.Profile;
import com.taxiticket.server.utils.PdfUtil;
import com.taxiticket.shared.BookingInfo;

public class PdfServlet extends HttpServlet
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    ProfileManager profileManager = new ProfileManager();
    BookingServiceImpl bookingService = new BookingServiceImpl();
    BookingManager bookingManager = new BookingManager();
    PdfUtil pdfUtil = new PdfUtil();
    private final Logger LOGGER = Logger.getLogger("PDFRendererServlet");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String order = req.getParameter("order");

        BookingInfo bookingInfo = bookingManager.getBookingInfo(Long.parseLong(order));
        Profile profile = profileManager.getProfile();
        bookingManager.sendConfirmationAndOrder(bookingInfo, profile);
        byte[] bytes = pdfUtil.generate(bookingInfo, new FileInputStream("template/Fahrtenscheck.pdf"), new FileInputStream("template/Rechnung.pdf"));
        resp.setContentType("application/pdf");
        resp.addHeader("Content-Disposition", "inline; filename=\"data.pdf\"");
        resp.setContentLength(bytes.length);

        ServletOutputStream sos = resp.getOutputStream();
        sos.write(bytes);
        sos.flush();
        sos.close();

    }

}
