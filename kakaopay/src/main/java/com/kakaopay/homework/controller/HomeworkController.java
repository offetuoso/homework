package com.kakaopay.homework.controller;


import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kakaopay.homework.dao.HomeworkRepository;
import com.kakaopay.homework.vo.Result;
import com.kakaopay.homework.entity.Spread;
import com.kakaopay.homework.service.HomeworkServiceImpl;


@RestController
public class HomeworkController {
	
	@Autowired
	HomeworkServiceImpl service;
	
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
