package com.taxiticket.server;

import java.util.Arrays;

import com.sofort.lib.DefaultSofortLibPayment;
import com.sofort.lib.SofortLibPayment;
import com.sofort.lib.internal.net.ConnectionException;
import com.sofort.lib.internal.net.http.HttpAuthorizationException;
import com.sofort.lib.internal.net.http.HttpConnectionException;
import com.sofort.lib.internal.transformer.RawResponse;
import com.sofort.lib.internal.transformer.RawResponse.Status;
import com.sofort.lib.products.request.PaymentRequest;
import com.sofort.lib.products.request.PaymentTransactionDetailsRequest;
import com.sofort.lib.products.request.parts.Notification;
import com.sofort.lib.products.response.PaymentResponse;
import com.sofort.lib.products.response.PaymentTransactionDetailsResponse;
import com.sofort.lib.products.response.SofortTransactionStatusNotification;
import com.sofort.lib.products.response.parts.PaymentTransactionDetails;
import com.taxiticket.server.entity.Config;
import com.taxiticket.server.entity.Profile;
import com.taxiticket.shared.BookingInfo;

/**
 * An example of usage of the SofortLib Payment (SOFORT Überweisung).
 */
public class SofortPaymentManager extends Manager
{

    public void initSofortPayment(BookingInfo bookingInfo, Config config, Profile profile)
    {
        Double amt = 0.10;
        if (config.getActive())
        {
            amt = ((double) (bookingInfo.getPrice()[2]) / 100);
        }

        /* initialize the default sofort lib payment */
        final int customerId = 113494;
        final int projectId = 233785;
        final String apiKey = "c61bdad24a9ea73ec6a77a7b9684a694";

        // FIXME
        // final String apiKey = profile.getSofortApiKey();

        final SofortLibPayment sofortLibPayment = new DefaultSofortLibPayment(customerId, apiKey);

        /*
         * 1st step -> start sofort payment, check for errors and warnings and
         * use the received payment url for redirection of the buyer
         */
        final String successUrl = config.getUrl() + "/ticket?order=" + bookingInfo.getId();
        PaymentRequest paymentRequest = new PaymentRequest(projectId, amt, "EUR", Arrays.asList("Customer 47110815", "Order " + bookingInfo.getId()), false);

        paymentRequest.setNotificationUrls(Arrays.asList(new Notification(config.getUrl())));
        paymentRequest.setSuccessUrl(successUrl);

        PaymentResponse paymentResponse;
        try
        {
            paymentResponse = sofortLibPayment.sendPaymentRequest(paymentRequest);
        }
        catch (HttpAuthorizationException e)
        {
            System.err.println("The authorization with the given apiKey has been failed.");
            throw e;

        }
        catch (HttpConnectionException e)
        {
            System.err.println("The HTTP communication has been failed. Response/status code: " + e.getResponseCode());
            throw e;

        }
        catch (ConnectionException e)
        {
            System.err.println("The communication has been failed.");
            throw e;
        }

        if (paymentResponse.hasResponseErrors() || paymentResponse.hasResponseWarnings())
        {
            // check and handle the response errors and warnings
            throw new RuntimeException();
        }

        if (paymentResponse.hasNewPaymentWarnings())
        {
            // check the new payment warnings
            throw new RuntimeException();
        }

        // start/resume/check listening on notificationUrls

        // Store or handle transId.
        System.out.println(paymentResponse.getTransId());
        // Redirect customer to payment URL.
        System.out.println(paymentResponse.getPaymentUrl());
        bookingInfo.setPaymentUrl(paymentResponse.getPaymentUrl());
        bookingInfo.setPaymentTransId(paymentResponse.getPaymentUrl());

        if (true)
            return;
        /*
         * 2nd step -> parse the received transaction changes notification
         */
        String statusNotification = /* received status notification */"";
        SofortTransactionStatusNotification statusNotificationResponse = sofortLibPayment.parseStatusNotificationResponse(new RawResponse(Status.OK, statusNotification));
        String statusNotificationTransId = statusNotificationResponse.getTransId();

        // handle notification responses
        System.out.println(statusNotificationTransId);

        /*
         * 3rd step -> get transaction details for notified transaction
         */
        PaymentTransactionDetailsRequest transactionRequest = new PaymentTransactionDetailsRequest()
                .setTransIds(Arrays.asList(statusNotificationTransId));

        PaymentTransactionDetailsResponse transactionDetailsResponse;
        try
        {
            transactionDetailsResponse = sofortLibPayment.sendTransactionDetailsRequest(transactionRequest);
        }
        catch (HttpConnectionException e)
        {

            if (e.getResponseCode() == 401)
            {
                System.err.println("The authorization with the given apiKey has been failed.");
            }

            throw e;
        }
        PaymentTransactionDetails detailsPayment = transactionDetailsResponse.getTransactions().get(0);

        // handle current status
        System.out.println(detailsPayment.getStatus() + " " + detailsPayment.getStatusReason());

        return;
    }

}
