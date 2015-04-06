package com.github.thiagolocatelli.paymill.model;

import java.util.Date;
import java.util.HashMap;

import com.github.thiagolocatelli.paymill.exception.APIConnectionException;
import com.github.thiagolocatelli.paymill.exception.APIException;
import com.github.thiagolocatelli.paymill.exception.AuthenticationException;
import com.github.thiagolocatelli.paymill.exception.InvalidRequestException;
import com.github.thiagolocatelli.paymill.exception.PreConditionFailedException;
import com.github.thiagolocatelli.paymill.exception.ResourceNotFoundException;
import com.github.thiagolocatelli.paymill.exception.TransactionErrorException;
import com.github.thiagolocatelli.paymill.net.APIResource;

public class Refund extends APIResource implements Webhookable {

	String id;
	String amount;
	String status;
	String description;
	Boolean livemode;
	Date createdAt;
	Date updatedAt;
	Integer responseCode;
	Transaction transaction;
	String appId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getLivemode() {
		return livemode;
	}

	public void setLivemode(Boolean livemode) {
		this.livemode = livemode;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Integer getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(Integer responseCode) {
		this.responseCode = responseCode;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public static Refund create(Integer amount, String description,
			String transactionId) throws AuthenticationException,
			InvalidRequestException, APIConnectionException, APIException,
			TransactionErrorException, PreConditionFailedException,
			ResourceNotFoundException {
		return create(amount, description, transactionId, null);
	}

	public static Refund create(Integer amount, String description,
			String transactionId, String apiKey)
			throws AuthenticationException, InvalidRequestException,
			APIConnectionException, APIException, TransactionErrorException,
			PreConditionFailedException, ResourceNotFoundException {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("amount", amount);
		if (description != null) {
			params.put("description", description);
		}
		return request(RequestMethod.POST,
				instanceURL(Refund.class, transactionId), params, Refund.class,
				apiKey);
	}

	public static Refund retrieve(String refundId)
			throws AuthenticationException, InvalidRequestException,
			APIConnectionException, APIException, TransactionErrorException,
			PreConditionFailedException, ResourceNotFoundException {
		return retrieve(refundId, null);
	}

	public static Refund retrieve(String refundId, String apiKey)
			throws AuthenticationException, InvalidRequestException,
			APIConnectionException, APIException, TransactionErrorException,
			PreConditionFailedException, ResourceNotFoundException {
		return request(RequestMethod.GET, instanceURL(Refund.class, refundId),
				null, Refund.class, apiKey);
	}

	public static RefundCollection list() throws AuthenticationException,
			InvalidRequestException, APIConnectionException, APIException,
			TransactionErrorException, PreConditionFailedException,
			ResourceNotFoundException {
		return list(null);
	}

	public static RefundCollection list(String apiKey)
			throws AuthenticationException, InvalidRequestException,
			APIConnectionException, APIException, TransactionErrorException,
			PreConditionFailedException, ResourceNotFoundException {
		return request(RequestMethod.GET, classURL(Refund.class), null,
				RefundCollection.class, apiKey);
	}
}
