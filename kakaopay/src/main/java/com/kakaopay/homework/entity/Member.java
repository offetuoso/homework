package com.kakaopay.homework.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class Member{
	
	//방 멤버 시퀀스
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int seq;
	
	//방번호
	private String roomId;
	
	//유저 아이디
	private int id;
	
	//닉네임
	private String nickName;
	
	@Override
	public String toString() {
		 return "Room [roomId=" + roomId + ", id=" + id+", nickName="+nickName+"]";
    }
	
	public int getSeq() {
		return seq;
	}



	public void setSeq(int seq) {
		this.seq = seq;
	}



	public String getRoomId() {
		return roomId;
	}



	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}



	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public String getNickName() {
		return nickName;
	}



	public void setNickName(String nickName) {
		this.nickName = nickName;
	}




	
	
}