package com.kakaopay.homework.entity;

import javax.persistence.Entity;
import javax.persistence.Id;



public class Result{
	
	private String status ;
	private String error ;
	private String message ;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	
	
}