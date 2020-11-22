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
        	
        	//금액감소 추가 
        	
        	Spread data = new Spread();
        	data.setRegId(id);
        	data.setRegDate(sdf.format(today));	//토큰별 동일한 시간 입력
        	data.setRoomId(roomId);
        	data.setAmount(amount);
        	data.setCnt(cnt);
        	data.setTotalAmount(amount);
        	data.setTotalCnt(cnt);
        	
        	calculator(data);
        	
        }
		
		
		List<Spread> list = repository.findCreatedSpreadList(roomId, id);
		
		
		String tokens = "";
		int regId = 0;
		int maxMoney = 0;
		int totalAmount = 0;
		int totalCnt = 0;
		StringBuffer temp = new StringBuffer();
		
		if(list != null) {
			for (int i = 0; i < list.size(); i++) {
				Spread data = list.get(i);
				System.out.println(data.toString());
				
				if(!tokens.equals("")) tokens+=",";
				tokens += "'"+data.getToken().toString()+"'";
				
				if(maxMoney < data.getMoney() )
					maxMoney = data.getMoney();
						
				if(i==0) {
					
					roomId = data.getRoomId();
					regId = data.getRegId();
					totalAmount = data.getTotalAmount();
					totalCnt = data.getTotalCnt();
					
				}
			}
		}
		
		temp.append("'roomId':'"+roomId+"',");
		temp.append("'regId':'"+regId+"',");
		temp.append("'amount':'"+totalAmount+"',");
		temp.append("'maxMoney':'"+maxMoney+"',");
		temp.append("'personCnt':'"+totalCnt+"',");
		temp.append("'token':["+tokens+"]");
		
		
		if(result.getStatus() == null) {
			result.setStatus("200");
			result.setMessage(temp.toString());
		}
		 
		 return result;
		 
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
	public Result takeMony(HttpServletRequest request, String token) {
		
		// parameter 없는경우 500 에러 처리
	
			int id = Integer.parseInt(request.getHeader("X-USER-ID").toString());
			String roomId = request.getHeader("X-ROOM-ID");
			
			Result result = new Result();
			
			int errorCnt = 0;

			Date today = new Date();
			SimpleDateFormat sdf;
			sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				
				
				
			//토큰있는지 조회 ; 뿌리기 시 발급된 token을 요청값으로 받습니다.
			Spread role1 = repository.findSecondRule1(roomId, token);
			
			if(role1== null){
				result.setStatus("5XX");
				result.setError("not found token");
				result.setMessage("유효하지 않은 token입니다.");
				errorCnt++;
			}
			
			//누구에게도 할당된건지 체크; token에 해당하는 뿌리기 건 중 아직 누구에게도 할당된건지 체크
			if(errorCnt == 0) {
				Spread role2 = repository.findSecondRule2(roomId, token);
				
				if(role1== null){
					result.setStatus("5XX");
					result.setError("already been used");
					result.setMessage("이미 사용된 토큰입니다.");
					errorCnt++;
				}
			}
			
			// 한번만 받았는지; 뿌리기 당 한 사용자는 한번만 받을 수 있습니다
			// 자신이등록한건 아닌지;  자신이 뿌리기한 건은 자신이 받을 수 없습니다.
			// 같은방 인원인지 ; 뿌린기가 호출된 대화방과 동일한 대화방에 속한 사용자만이 받을 수
			//10분 리미트 ;뿌린 건은 10분간만 유효합니다. 뿌린지 10분이 지난 요청에 대해서는 받기실패 응답이 내려가야 합니다.
				
				/*
				 * 
				 * ● 다음 조건을 만족하는 조회 API를 만들어 주세요.
						○ 뿌리기 시 발급된 token을 요청값으로 받습니다.
						○ token에 해당하는 뿌리기 건의 현재 상태를 응답값으로 내려줍니다. 현재
						상태는 다음의 정보를 포함합니다.
						○ 뿌린 시각, 뿌린 금액, 받기 완료된 금액, 받기 완료된 정보 ([받은 금액, 받은
						사용자 아이디] 리스트)
						○ 뿌린 사람 자신만 조회를 할 수 있습니다. 다른사람의 뿌리기건이나 유효하지
						않은 token에 대해서는 조회 실패 응답이 내려가야 합니다.
						○ 뿌린 건에 대한 조회는 7일 동안 할 수 있습니다.

				 * 
				 * */
				
				
				
				
				if(errorCnt == 0) {
					
					
					
				}
				
				if(errorCnt== 0 && result.getStatus() == null) {
					result.setStatus("200");
					result.setMessage("");
				}
				
				return result;
		 
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
	@PostMapping("/homework/Third")
	public Result getHistory(HttpServletRequest request, String token) {
		
		// parameter 없는경우 500 에러 처리
		
		int id = Integer.parseInt(request.getHeader("X-USER-ID").toString());
		String roomId = request.getHeader("X-ROOM-ID");
		
		Result result = new Result();
		
		int errorCnt = 0;

		Date today = new Date();
		SimpleDateFormat sdf;
		sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		
		
		
	//토큰있는지 조회 ; 뿌리기 시 발급된 token을 요청값으로 받습니다.
	Spread role1 = repository.findSecondRule1(roomId, token);
	
	if(role1== null){
		result.setStatus("5XX");
		result.setError("not found token");
		result.setMessage("유효하지 않은 token입니다.");
		errorCnt++;
	}
	
	//누구에게도 할당된건지 체크; token에 해당하는 뿌리기 건 중 아직 누구에게도 할당된건지 체크
	if(errorCnt == 0) {
		Spread role2 = repository.findSecondRule2(roomId, token);
		
		if(role1== null){
			result.setStatus("5XX");
			result.setError("already been used");
			result.setMessage("이미 사용된 토큰입니다.");
			errorCnt++;
		}
	}
	
	// 한번만 받았는지; 뿌리기 당 한 사용자는 한번만 받을 수 있습니다
	// 자신이등록한건 아닌지;  자신이 뿌리기한 건은 자신이 받을 수 없습니다.
	// 같은방 인원인지 ; 뿌린기가 호출된 대화방과 동일한 대화방에 속한 사용자만이 받을 수
	//10분 리미트 ;뿌린 건은 10분간만 유효합니다. 뿌린지 10분이 지난 요청에 대해서는 받기실패 응답이 내려가야 합니다.
		
		/*
		 * 
		 * ● 다음 조건을 만족하는 조회 API를 만들어 주세요.
				○ 뿌리기 시 발급된 token을 요청값으로 받습니다.
				○ token에 해당하는 뿌리기 건의 현재 상태를 응답값으로 내려줍니다. 현재
				상태는 다음의 정보를 포함합니다.
				○ 뿌린 시각, 뿌린 금액, 받기 완료된 금액, 받기 완료된 정보 ([받은 금액, 받은
				사용자 아이디] 리스트)
				○ 뿌린 사람 자신만 조회를 할 수 있습니다. 다른사람의 뿌리기건이나 유효하지
				않은 token에 대해서는 조회 실패 응답이 내려가야 합니다.
				○ 뿌린 건에 대한 조회는 7일 동안 할 수 있습니다.

		 * 
		 * */
		
		
		
		
		if(errorCnt == 0) {
			
			
			
		}
		
		if(errorCnt== 0 && result.getStatus() == null) {
			result.setStatus("200");
			result.setMessage("");
		}
		
		return result;
		
	}
	
	

	private void calculator(Spread data) {
		int amount = data.getAmount(); 	//입력금액
		int cnt = data.getCnt();		//수령인 숫자
		int money = 0;					//수령금액
		int regId = data.getRegId();
		int maxMoney = data.getRegId();
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
            result.setTotalAmount(data.getTotalAmount());
            result.setTotalCnt(data.getTotalCnt());
           
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
		        temp.append((int)(Math.random() * (9) + 1 ));
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
