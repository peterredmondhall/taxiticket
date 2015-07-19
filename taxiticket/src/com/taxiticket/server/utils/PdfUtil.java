package com.taxiticket.server.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;

import com.google.common.collect.Lists;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSmartCopy;
import com.itextpdf.text.pdf.PdfStamper;
import com.taxiticket.shared.BookingInfo;

public class PdfUtil
{
    private final Logger log = Logger.getLogger("PdfUtil");

    private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    static final float TABLE_WIDTH = 370; // 452
    static final float PAYMENT_TABLE_Y = 270; // 400
    static final float DETAIL_TABLE_Y = 465; // 400
    static final float INSET = 80;
    static final float DETAIL_INSET = 170;
    static final float RECEIPT_DETAIL_Y = 560;

    static final float PICKUP_INSET = 110;
    static final float PICKUP_Y = 545;

    static final float DATE_INSET = 80;
    static final float DATE_Y = 580;

    static final float NAME_INSET = 210;
    static final float NAME_Y = 370;

    NumberFormat currencyFormatter =
            NumberFormat.getCurrencyInstance();

    public byte[] generate(BookingInfo bookingInfo, FileInputStream ticket, FileInputStream receipt)
    {
        try
        {
            Document doc = new Document();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PdfSmartCopy copy = new PdfSmartCopy(doc, bos);
            doc.open();

            byte[] generateTicket = generateTicket(ticket, bookingInfo);
            for (int taxi = 1; taxi <= bookingInfo.getNumTaxis(); taxi++)
            {
                PdfReader taxiReader = new PdfReader(generateTicket);
                copy.addPage(copy.getImportedPage(taxiReader, 1));
            }
            PdfReader reader = new PdfReader(generateReceipt(receipt, bookingInfo));
            copy.addPage(copy.getImportedPage(reader, 1));

            doc.close();
            return bos.toByteArray();
        }
        catch (Exception e)
        {
            String msg = "error generating schecks";
            log.severe(msg);
            e.printStackTrace();
        }
        return null;
    }

    private byte[] generateTicket(FileInputStream fis, BookingInfo bookingInfo)
    {
        PdfReader reader;
        try
        {
            reader = new PdfReader(IOUtils.toByteArray(fis));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfStamper stamper = new PdfStamper(reader, out);
            PdfContentByte canvas = stamper.getOverContent(1);

            Font helvetica = new Font(FontFamily.TIMES_ROMAN, 14);
            BaseFont bf_helv = helvetica.getCalculatedBaseFont(false);

            ColumnText.showTextAligned(canvas,
                    Element.ALIGN_LEFT, new Phrase(new Chunk(bookingInfo.getDateText(), helvetica)), DATE_INSET, DATE_Y, 0);
            ColumnText.showTextAligned(canvas,
                    Element.ALIGN_LEFT, new Phrase(new Chunk(bookingInfo.getPickup(), helvetica)), PICKUP_INSET, PICKUP_Y, 0);
            ColumnText.showTextAligned(canvas,
                    Element.ALIGN_LEFT, new Phrase(new Chunk("nach", helvetica)), PICKUP_INSET, PICKUP_Y - 20, 0);
            ColumnText.showTextAligned(canvas,
                    Element.ALIGN_LEFT, new Phrase(new Chunk(bookingInfo.getDropoff(), helvetica)), PICKUP_INSET, PICKUP_Y - 40, 0);
            ColumnText.showTextAligned(canvas,
                    Element.ALIGN_LEFT, new Phrase(new Chunk(bookingInfo.getName(), helvetica)), NAME_INSET, NAME_Y, 0);

            stamper.close();
            reader.close();
            fis.close();

            return out.toByteArray();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (DocumentException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public byte[] generateReceipt(FileInputStream fis, BookingInfo bookingInfo)
    {
        PdfReader reader;
        try
        {
            reader = new PdfReader(IOUtils.toByteArray(fis));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfStamper stamper = new PdfStamper(reader, out);
            PdfContentByte canvas = stamper.getOverContent(1);

            PdfPTable detailTable = createDetailsTable(bookingInfo);
            detailTable.writeSelectedRows(0, 2, 0, 11, INSET, DETAIL_TABLE_Y, canvas);

            PdfPTable paymentTable = createPaymentTable(bookingInfo);
            paymentTable.writeSelectedRows(0, 2, 0, 11, INSET, PAYMENT_TABLE_Y, canvas);

            Font helvetica = new Font(FontFamily.TIMES_ROMAN, 14);
            BaseFont bf_helv = helvetica.getCalculatedBaseFont(false);
            Chunk c1 = new Chunk(Long.toString(bookingInfo.getId()), helvetica);
            Chunk dateChunk = new Chunk(sdf.format(new Date()), helvetica);

            ColumnText.showTextAligned(canvas,
                    Element.ALIGN_LEFT, new Phrase(c1), DETAIL_INSET, RECEIPT_DETAIL_Y, 0);
            ColumnText.showTextAligned(canvas,
                    Element.ALIGN_LEFT, new Phrase(dateChunk), DETAIL_INSET + 200, RECEIPT_DETAIL_Y, 0);

            stamper.close();
            reader.close();
            fis.close();

            return out.toByteArray();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (DocumentException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public PdfPTable createDetailsTable(BookingInfo bookingInfo)
    {
        List<String[]> values = Lists.newArrayList();
        values.add(new String[] { "Tag des Transports", bookingInfo.getDateText() });
        values.add(new String[] { "Name", bookingInfo.getName() });
        values.add(new String[] { "Von", bookingInfo.getPickup() });
        values.add(new String[] { "Nach", bookingInfo.getDropoff() });

        return createTable(values);
    }

    public PdfPTable createPaymentTable(BookingInfo bookingInfo)
    {
        List<String[]> values = Lists.newArrayList();
        values.add(new String[] { "Transport", "" });

        if (false)
        {
            values.add(new String[] { "Brutto", currencyFormatter.format(bookingInfo.getPrice()[0] / 100) });
            values.add(new String[] { "Mwst", currencyFormatter.format(bookingInfo.getPrice()[1] / 100) });
            values.add(new String[] { "Netto", currencyFormatter.format(bookingInfo.getPrice()[2] / 100) });

        }
        else
        {
            values.add(new String[] { "Brutto", currencyFormatter.format(0) });
            values.add(new String[] { "Mwst", currencyFormatter.format(0) });
            values.add(new String[] { "Netto", currencyFormatter.format(0) });

        }

        return createTable(values);
    }

    public PdfPTable createTable(List<String[]> values)
    {
        PdfPTable table = new PdfPTable(2);
        table.setTotalWidth(TABLE_WIDTH);
        table.setWidthPercentage(TABLE_WIDTH / 6f);
        try
        {
            table.setWidths(new int[] { 1, 2 });
        }
        catch (DocumentException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // the cell object
        PdfPCell cell;
        for (String[] pair : values)
        {
            table.addCell((String) pair[0]);
            table.addCell((String) pair[1]);

        }

        return table;
    }

}
