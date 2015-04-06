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
import com.github.thiagolocatelli.paymill.net.VoidResponse;

public class Payment extends APIResource {

	String id;
	String type;
	String client;
	String code;
	String holder;
	String cardType;
	String country;
	String expireMonth;
	String expireYear;
	String cardHolder;
	String last4;
	Date createdAt;
	Date updatedAt;
	String appId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getExpireMonth() {
		return expireMonth;
	}

	public void setExpireMonth(String expireMonth) {
		this.expireMonth = expireMonth;
	}

	public String getExpireYear() {
		return expireYear;
	}

	public void setExpireYear(String expireYear) {
		this.expireYear = expireYear;
	}

	public String getCardHolder() {
		return cardHolder;
	}

	public void setCardHolder(String cardHolder) {
		this.cardHolder = cardHolder;
	}

	public String getLast4() {
		return last4;
	}

	public void setLast4(String last4) {
		this.last4 = last4;
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

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}
	
	public static Payment createWithToken(String token)
			throws AuthenticationException, InvalidRequestException,
			APIConnectionException, APIException, TransactionErrorException,
			PreConditionFailedException, ResourceNotFoundException {
		return createWithTokenAndClient(token, null, null);
	}

	public static Payment createWithToken(String token, String apiKey)
			throws AuthenticationException, InvalidRequestException,
			APIConnectionException, APIException, TransactionErrorException,
			PreConditionFailedException, ResourceNotFoundException {
		return createWithTokenAndClient(token, null, apiKey);
	}
	
	public static Payment createWithTokenAndClient(String token, String client)
			throws AuthenticationException, InvalidRequestException,
			APIConnectionException, APIException, TransactionErrorException,
			PreConditionFailedException, ResourceNotFoundException {
		return createWithTokenAndClient(token, client, null);
	}
	
	public static Payment createWithTokenAndClient(String token, String client, String apiKey)
			throws AuthenticationException, InvalidRequestException,
			APIConnectionException, APIException, TransactionErrorException,
			PreConditionFailedException, ResourceNotFoundException {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("token", token);
		if(client != null) {
			params.put("client", client);
		}
		return request(RequestMethod.POST, classURL(Payment.class), params,
				Payment.class, apiKey);
	}

	public static Payment retrieve(String id) throws AuthenticationException,
			InvalidRequestException, APIConnectionException, APIException,
			TransactionErrorException, PreConditionFailedException,
			ResourceNotFoundException {
		return retrieve(id, null);
	}

	public static Payment retrieve(String id, String apiKey)
			throws AuthenticationException, InvalidRequestException,
			APIConnectionException, APIException, TransactionErrorException,
			PreConditionFailedException, ResourceNotFoundException {
		return request(RequestMethod.GET, instanceURL(Payment.class, id), null,
				Payment.class, apiKey);
	}

	public void delete(String apiKey) throws AuthenticationException,
			InvalidRequestException, APIConnectionException, APIException,
			TransactionErrorException, PreConditionFailedException,
			ResourceNotFoundException {
		request(RequestMethod.DELETE, instanceURL(Payment.class, getId()),
				null, VoidResponse.class, apiKey);
	}

	public void delete() throws AuthenticationException,
			InvalidRequestException, APIConnectionException, APIException,
			TransactionErrorException, PreConditionFailedException,
			ResourceNotFoundException {
		delete(null);
	}

	public static PaymentCollection list() throws AuthenticationException,
			InvalidRequestException, APIConnectionException, APIException,
			TransactionErrorException, PreConditionFailedException,
			ResourceNotFoundException {
		return list(null);
	}

	public static PaymentCollection list(String apiKey)
			throws AuthenticationException, InvalidRequestException,
			APIConnectionException, APIException, TransactionErrorException,
			PreConditionFailedException, ResourceNotFoundException {
		return request(RequestMethod.GET, classURL(Payment.class), null,
				PaymentCollection.class, apiKey);
	}

}
