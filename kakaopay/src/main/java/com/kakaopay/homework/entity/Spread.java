package com.kakaopay.homework.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.ColumnDefault;

import lombok.EqualsAndHashCode;


@Entity
public class Spread{
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int seq;
	
	@Column(nullable = true)
	private String token;
	@Column(nullable = true)
	private String roomId;
	
	private int regId;
	@Column(nullable = true)
	private String regDate;
	
	@ColumnDefault("0")
	private int receivId;
	
	@Column(nullable = true)
	private String recivDate;	
		
	@ColumnDefault("0")
	private int amount;			//입력금액
	
	@ColumnDefault("0")		  
	private int cnt;			//수령인 숫자
	
	@ColumnDefault("0")
	private int money;			//수령금액
	
	@ColumnDefault("0")
	private int totalAmount;	//최초 입력금액
	
	@ColumnDefault("0")
	private int totalCnt;		//총 수령인
	
	@Column(nullable = true)
	private String nickName;

	@Column(nullable = true)
	private String roomName;
	
	@ColumnDefault("0")
	private int maxMoney;
	
	
	

	@Override
	public String toString() {
		return "Spread [seq=" + seq + ", token=" + token + ", roomId=" + roomId + ", regId=" + regId + ", regDate="
				+ regDate + ", receivId=" + receivId + ", recivDate=" + recivDate + ", amount=" + amount + ", cnt="
				+ cnt + ", money=" + money + ", totalAmount=" + totalAmount + ", totalCnt=" + totalCnt + ", nickName="
				+ nickName + ", roomName=" + roomName + ", maxMoney=" + maxMoney + "]";
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

	public int getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(int totalAmount) {
		this.totalAmount = totalAmount;
	}

	public int getTotalCnt() {
		return totalCnt;
	}

	public void setTotalCnt(int totalCnt) {
		this.totalCnt = totalCnt;
	}
	
	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getRoomName() {
		return roomName;
	}
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	public int getMaxMoney() {
		return maxMoney;
	}
	public void setMaxMoney(int maxMoney) {
		this.maxMoney = maxMoney;
	}
}