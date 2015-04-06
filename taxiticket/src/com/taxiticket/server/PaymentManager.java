package com.taxiticket.server;

import java.util.HashMap;

import com.github.thiagolocatelli.paymill.exception.BridgeException;
import com.github.thiagolocatelli.paymill.model.Bridge;
import com.paymill.context.PaymillContext;
import com.paymill.models.Payment;
import com.paymill.models.Transaction;
import com.paymill.services.PaymentService;
import com.paymill.services.PreauthorizationService;
import com.paymill.services.TransactionService;
import com.taxiticket.server.entity.Profile;
import com.taxiticket.shared.BookingInfo;

public class PaymentManager {
//	private static String PrivateKey = "7d4190f0708d6d5f88d13e847165a2e8";
//	private static String PublicKey = "379663699274b8d8cb0f973451213cd2";

	private static HashMap<String, Object> ibanParams;

	  private String                  currency    = "EUR";
	  private String                  description = "taxiticket";

	  private TransactionService      transactionService;
	  private PaymentService          paymentService;
	  private Payment                 payment;

	public Transaction pay(BookingInfo bookingInfo, Profile profile)
	{
			ibanParams = new HashMap<String, Object>();
			ibanParams.put("account.iban", "DE12500105170648489890");
			ibanParams.put("account.bic", "BENEDEPPYYY");
			ibanParams.put("account.holder", "Alex Tabo");
		
			String token=null;
			
			try {
				token = Bridge.create(profile.getPaymillPublishable(), ibanParams);
			} catch (BridgeException e) {
				// TODO Auto-generated catch block
				bookingInfo.setPaymentSuccessful(false);
				bookingInfo.setPaymentResponseCode(e.getError());
				return null;
			}

			   PaymillContext paymill = new PaymillContext( profile.getPaymitllSecret() );

			    this.transactionService = paymill.getTransactionService();
			    this.paymentService = paymill.getPaymentService();
			    this.payment = this.paymentService.createWithToken( token );
			 
		    Transaction transaction = this.transactionService.createWithPayment( this.payment, (int)bookingInfo.getPrice()[2], this.currency, this.description );
		    
		    return transaction;
	}

}
