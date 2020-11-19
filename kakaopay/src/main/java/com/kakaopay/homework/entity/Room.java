package com.kakaopay.homework.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class Room{
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int seq;
	private String roomId;
	private String roomName;
	
	@Override
	public String toString() {
		return "Room [seq=" + seq + ", roomId=" + roomId + ", roomName=" + roomName + "]";
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

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	
	
}