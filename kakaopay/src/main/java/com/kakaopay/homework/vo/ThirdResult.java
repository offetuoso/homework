package com.kakaopay.homework.vo;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;



public class ThirdResult extends Result{
	
	private String regDate; 
	private int spreadAmount;
	private int receivedAmount;
	private List<RecieveInfo> receivedList;
	
	public String getRegDate() {
		return regDate;
	}
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	public int getSpreadAmount() {
		return spreadAmount;
	}
	public void setSpreadAmount(int spreadAmount) {
		this.spreadAmount = spreadAmount;
	}
	public int getReceivedAmount() {
		return receivedAmount;
	}
	public void setReceivedAmount(int receivedAmount) {
		this.receivedAmount = receivedAmount;
	}
	public List<RecieveInfo> getReceivedList() {
		return receivedList;
	}
	public void setReceivedList(List<RecieveInfo> receivedList) {
		this.receivedList = receivedList;
	}
	
	
		
}