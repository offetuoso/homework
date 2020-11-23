package com.kakaopay.homework.vo;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;



public class FirstResult extends Result{
	
	
	private String roomId;
	private int regId;
	private int amount;
	private int maxMoney;
	private int personCnt;
	private String token;
	private List<Integer> idxList;
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	public int getRegId() {
		return regId;
	}
	public void setRegId(int regId) {
		this.regId = regId;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public int getMaxMoney() {
		return maxMoney;
	}
	public void setMaxMoney(int maxMoney) {
		this.maxMoney = maxMoney;
	}
	public int getPersonCnt() {
		return personCnt;
	}
	public void setPersonCnt(int personCnt) {
		this.personCnt = personCnt;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public List<Integer> getIdx() {
		return idxList;
	}
	public void setIdx(List<Integer> idxList) {
		this.idxList = idxList;
	}
	
	
}