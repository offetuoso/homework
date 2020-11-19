package com.kakaopay.homework.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.EqualsAndHashCode;


@Entity
public class Spread{
	
	
	@Id
	private String token;
	
	private String roomId;
	
	private int regId;
	
	private String regDate;
	
	@Column(nullable = true)
	private int receivId;
	
	@Column(nullable = true)
	private String recivDate;	
	
	@Column(nullable = true)
	private int amount;			//입력금액
	
	@Column(nullable = true)
	private int cnt;			//수령인 숫자
	
	private int money;			//수령금액
	
	
	
	@Override
	public String toString() {
		return "Spread [token=" + token + ", roomId=" + roomId + ", regId=" + regId + ", regDate=" + regDate
				+ ", receivId=" + receivId + ", recivDate=" + recivDate + ", amount=" + amount + ", cnt=" + cnt
				+ ", money=" + money + "]";
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

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

	public String getRegDate() {
		return regDate;
	}

	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}

	public int getReceivId() {
		return receivId;
	}

	public void setReceivId(int receivId) {
		this.receivId = receivId;
	}

	public String getRecivDate() {
		return recivDate;
	}

	public void setRecivDate(String recivDate) {
		this.recivDate = recivDate;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getCnt() {
		return cnt;
	}

	public void setCnt(int cnt) {
		this.cnt = cnt;
	}
	
	
}