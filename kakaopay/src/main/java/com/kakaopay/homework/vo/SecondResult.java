package com.kakaopay.homework.vo;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;



public class SecondResult extends Result{
	
	private String roomId;
	private String token;
	private int money;
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
		
}