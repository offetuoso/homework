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
	 *              
	 * */
	@PostMapping("/homework/first")
	public Result first(HttpServletRequest request, int amount, int cnt) {
		
		
		//header에서 X-USER-ID, X-ROOM-ID 받아옴
		int id = Integer.parseInt(request.getHeader("X-USER-ID").toString());
		String roomId = request.getHeader("X-ROOM-ID");
		

		//토큰 그룹별 생성일 
		Date today = new Date();
    	SimpleDateFormat sdf;
    	sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    	
    	//뿌리기 엔티티, id, 생성일, 방번호, 인원 추가
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
	 * uri			= /homework/second
	 * header 		= int 	 X-USER-ID	//유저 id
	 *              , String X-ROOM-ID 	//방 id
	 *                             
	 * param		= Stromg token 		//받을 토큰
	 * */
	@PostMapping("/homework/second")
	public Result second(HttpServletRequest request, String token) {
		
			//header에서 X-USER-ID, X-ROOM-ID 받아옴
			int id = Integer.parseInt(request.getHeader("X-USER-ID").toString());
			String roomId = request.getHeader("X-ROOM-ID");
			
			//받을 아이디, 토큰, 방번호
			Spread data = new Spread();
			data.setReceiveId(id);	
			data.setToken(token);
			data.setRoomId(roomId);
			
			return service.secondProccess(data);
		 
	}
	
	
	/***
	 * req_type 	= post
	 * uri			= /homework/third
	 * header 		= int 	 X-USER-ID	//유저 id
	 *                             
	 * param		= String token 		//조회할 토큰
	 * */
	@PostMapping("/homework/third")
	public Result third(HttpServletRequest request, String token) {
		
		//header에서 X-USER-ID 받아옴
		int id = Integer.parseInt(request.getHeader("X-USER-ID").toString());
		
		//아이디, 토큰
		Spread data = new Spread();
		data.setReceiveId(id);
		data.setToken(token);
		
		return service.thirdProccess(data);
		
	}
	
	

	
	
	
	
	
}
