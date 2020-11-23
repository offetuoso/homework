package com.kakaopay.homework.vo;

import javax.persistence.Entity;
import javax.persistence.Id;



public class RecieveInfo{
	int amount;
	int receieve_id;
	
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public int getReceieve_id() {
		return receieve_id;
	}
	public void setReceieve_id(int receieve_id) {
		this.receieve_id = receieve_id;
	}
	
	
}