package com.kakaopay.homework.controller;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kakaopay.homework.dao.CustomerRepository;
import com.kakaopay.homework.dao.SpreadRepository;
import com.kakaopay.homework.entity.Customer;
import com.kakaopay.homework.entity.Result;
import com.kakaopay.homework.entity.Spread;

import ch.qos.logback.classic.spi.PackagingDataCalculator;
import lombok.Data;

@RestController
public class HomeworkController {
	//private CustomerRepository repository;
	private SpreadRepository repository;

	public HomeworkController(SpreadRepository repository) {
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
    	
		if(amount < cnt){
			result.setStatus("5XX");
			result.setError("more than Amount");
			result.setMessage("인원수 보다 많은 금액을 입력하세요.");
			
        }else {
        	
        	Spread data = new Spread();
        	data.setRegId(id);
        	data.setRoomId(roomId);
        	data.setAmount(amount);
        	
       
        	
        	data.setRegDate(sdf.format(today));
        	data.setCnt(cnt);
        	calculator(data);
        	
        }
		
		
		repository.findCreatedSpreadList(roomId, id);
		
		if(result.getStatus() == null) {
			result.setStatus("200");
			result.setMessage("성공");
		}
		 
		 return result;
		 
	}
	
	
	private void calculator(Spread data) {
		int amount = data.getAmount(); 	//입력금액
		int cnt = data.getCnt();		//수령인 숫자
		int money = 0;					//수령금액
		int regId = data.getRegId();
		String roomId = data.getRoomId();
		String regDate = data.getRegDate();
		String token = null;
		Spread obj = new Spread();
		
		if(cnt != 0){
            
			/*
             * 		수령금액 생
             * */
			
            if(cnt !=1 ){
                money = (int)(Math.random() * (amount- cnt) + 1 ) ;
            }else{
                money = amount;
            }
            
            
            /*
             * 		토큰 생성후 사용중인 토큰이 나오면 다시 생성
             * */
            Spread result = new Spread(); 
            
            while(obj != null) {
            	token = createToken(result);
            	result.setToken(token);
            	
            	obj = repository.findToken(roomId, token );
            	
            }
            
            result.setMoney(money);
            result.setToken(token);
            result.setRegId(regId);
            result.setRoomId(roomId);
            result.setRegDate(regDate);
            
            
            /*
             * 		결과 저장 
             * */
            repository.save(result);     
            
            result.setAmount(amount-money);
            result.setCnt(cnt-1);
            calculator(result);
		}
		
	}
	
	
	private String createToken(Spread result) {
		String roomId = result.getRoomId();
		
		StringBuffer temp = new StringBuffer();
		for (int i = 0; i < 3; i++) {
		    int rIndex = (int)(Math.random() * (3));
		    switch (rIndex) {
		    case 0:
		        // a-z
		        temp.append((char)((int)(Math.random()*26)+97));
		        break;
		    case 1:
		        // A-Z
		        temp.append((char)((int)(Math.random()*26)+65));
		        break;
		    case 2:
		        // 0-9
		        temp.append((int)(Math.random() * (10) + 1 ));
		        break;
		    }
		}
		
		return temp.toString();
	}
	
	
	
	@GetMapping("/spread/list")
	public List<Spread> allSpread() {
		
		return (List<Spread>) repository.findAll();
		
	}
	
	
	
}
