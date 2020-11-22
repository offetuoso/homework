package com.kakaopay.homework.controller;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kakaopay.homework.dao.HomeworkRepository;
import com.kakaopay.homework.entity.Customer;
import com.kakaopay.homework.entity.Result;
import com.kakaopay.homework.entity.Spread;
import com.kakaopay.homework.service.HomeworkService;
import com.kakaopay.homework.service.HomeworkServiceImpl;

import ch.qos.logback.classic.spi.PackagingDataCalculator;
import lombok.Data;

@RestController
public class HomeworkController {
	private HomeworkRepository repository;
	
	@Autowired
	HomeworkServiceImpl service;

	public HomeworkController(HomeworkRepository repository) {
		super();
		this.repository = repository;
	}
	
	/***
	 * req_type 	= post
	 * uri			= /homework/first
	 * header 		= int 	 X-USER-ID	//유저 id
	 *              , String X-ROOM-ID 	//방 id
	 *                             
	 * param		= int    money 		//뿌릴돈
	 *              , int    cnt   		//받을 인원수
	 * */
	@PostMapping("/homework/first")
	public Result spread(HttpServletRequest request, int amount, int cnt) {
		
		// parameter 없는경우 500 에러 처리
		
		int id = Integer.parseInt(request.getHeader("X-USER-ID").toString());
		String roomId = request.getHeader("X-ROOM-ID");
		Result result = new Result();
		
		Date today = new Date();
    	SimpleDateFormat sdf;
    	sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    	
    	Spread data = new Spread();
    	data.setRegId(id);				
    	data.setRegDate(sdf.format(today));	//토큰별 동일한 시간 입력
    	data.setRoomId(roomId);
    	data.setTotalAmount(amount);
    	data.setTotalCnt(cnt);
		 
		return service.firstProccess(data);
		 
	}
	
	
	/***
	 * req_type 	= post
	 * uri			= /homework/first
	 * header 		= int 	 X-USER-ID	//유저 id
	 *              , String X-ROOM-ID 	//방 id
	 *                             
	 * param		= int    money 		//뿌릴돈
	 *              , int    cnt   		//받을 인원수
	 * */
	@PostMapping("/homework/second")
	public Result takeMoney(HttpServletRequest request, String token) {
		
		// parameter 없는경우 500 에러 처리
	
			int id = Integer.parseInt(request.getHeader("X-USER-ID").toString());
			String roomId = request.getHeader("X-ROOM-ID");
			
			Spread data = new Spread();
			data.setReceiveId(id);
			data.setToken(token);
			data.setRoomId(roomId);
			
			return service.secondProccess(data);
		 
	}
	
	
	/***
	 * req_type 	= post
	 * uri			= /homework/first
	 * header 		= int 	 X-USER-ID	//유저 id
	 *              , String X-ROOM-ID 	//방 id
	 *                             
	 * param		= int    money 		//뿌릴돈
	 *              , int    cnt   		//받을 인원수
	 * */
	@PostMapping("/homework/third")
	public Result getHistory(HttpServletRequest request, String token) {
		
		// parameter 없는경우 500 에러 처리
		
		int id = Integer.parseInt(request.getHeader("X-USER-ID").toString());
		String roomId = request.getHeader("X-ROOM-ID");
		
	
		Spread data = new Spread();
		data.setReceiveId(id);
		data.setToken(token);
		data.setRoomId(roomId);
		
		return service.thirdProccess(data);
		
	}
	
	

	
	
	
	
	
}
