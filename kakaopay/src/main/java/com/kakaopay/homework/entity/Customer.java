package com.kakaopay.homework.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class Customer  {
	
	//유저아이디
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	//이름
	private String name;
	
	//계좌잔액
	private int amount;
	
	@Override
	public String toString() {
		 return "Customer [id=" + id + ", name=" + name + ", amount"+amount+"]";
    }
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	
	
	
}