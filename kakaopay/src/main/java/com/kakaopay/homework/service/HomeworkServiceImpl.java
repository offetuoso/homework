package com.kakaopay.homework.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.RollbackException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kakaopay.homework.dao.HomeworkRepository;
import com.kakaopay.homework.vo.Errors;
import com.kakaopay.homework.vo.FirstResult;
import com.kakaopay.homework.vo.RecieveInfo;
import com.kakaopay.homework.vo.Result;
import com.kakaopay.homework.vo.SecondResult;
import com.kakaopay.homework.entity.Spread;
import com.kakaopay.homework.vo.ThirdResult;

@Service("HomeworkService")
public class HomeworkServiceImpl implements HomeworkService{
	@Autowired
	private HomeworkRepository repository;
	
	
	// 뿌리기 기능
	public Result firstProccess(Spread spread) {
		
		//데이터 세팅
		FirstResult result = new FirstResult();
		Errors errors  = new Errors();
		StringBuffer msg = new StringBuffer();
		List<Integer> idxs = new ArrayList<>();
		
		String roomId = spread.getRoomId();
		int id = spread.getRegId();
		String token = "";
		int maxMoney = 0;
		int totalAmount = spread.getTotalAmount();
		int totalCnt = spread.getTotalCnt();
		
		spread.setAmount(totalAmount);	//분배 시 남은금액
		spread.setCnt(totalCnt);		//분배 시 남은 인원
		
		
		//분배로직 
		calculator(spread);
		
		//분배후 조회
    	List<Spread> list = repository.findCreatedSpreadList(roomId, id);
    	
    	//인원보다 금액이 적으면 분배 할 수 없음
    	if(totalAmount < totalCnt){
    		errors.setStatus("5XX");
    		errors.setError("more than Amount");
    		errors.setMessage("인원수 보다 많은 금액을 입력하세요.");
    		
    		result.setSuccess(false);
    		result.setErrors(errors);
			
        }else {
        	
        	//분배가능
        	if(list != null) {
        		
        		//출금 - 본인 AMOUNT - 뿌리기금액
        		repository.withdrawal(id, totalAmount);
        		
        		
        		//결과 데이터 생성
        		for (int i = 0; i < list.size(); i++) {
        			Spread data = list.get(i);
        			
        			idxs.add(data.getCnt());
        			
        			if(maxMoney < data.getMoney() )
        				maxMoney = data.getMoney();
        			
        			if(i==0)
        				token = data.getToken();
        		}
    
        		result.setSuccess(true);
        		
    			errors.setStatus("200");
    			errors.setError("success");;
    			result.setErrors(errors);
        		result.setRegId(id);
        		result.setRoomId(roomId);
        		result.setAmount(totalAmount);
    			result.setMaxMoney(maxMoney);
    			result.setPersonCnt(totalAmount);
    			result.setToken(token);
    			result.setIdx(idxs);
    			
    			/*
    			 * {
					    "success": true,
					    "errors": {
					        "status": "200",
					        "error": "success",
					        "message": null
					    },
					    "roomId": "R0001",
					    "regId": 1,
					    "amount": 2000,
					    "maxMoney": 1103,
					    "personCnt": 2000,
					    "token": "sz2",
					    "idx": [
					        0,
					        1,
					        2,
					        3,
					        4
					    ]
					}
    			 */
        		
        	}else {
        		
        		//분배후 결과 못찾음 
        		errors.setStatus("5XX");
        		errors.setError("not found result.");
        		errors.setMessage("결과를 찾을 수 없습니다.");
        		
        		result.setSuccess(false);
        		result.setErrors(errors);
        	}
        }
    	
    	return result;
	}

	//분배로직 인원수 만큼 반복, 
	//인원수 ~ 입력금액까지 수령금생성
	//받은금액-수령금 과 인원수-1로 다시호출
	public void calculator(Spread data) {
		int amount = data.getAmount(); 	//입력금액
		int cnt = data.getCnt();		//수령인 숫자
		int money = 0;					//수령금액
		int regId = data.getRegId();
		String roomId = data.getRoomId();
		String regDate = data.getRegDate();
		String token = data.getToken();
		Spread obj = new Spread();
		
		
		//재귀로 수령인 숫자 0이 아닐때만 수행
		if(cnt != 0){
	        
			//수령금액 생성 
	        if(cnt !=1 ){
	        	// 수령인수 ~ 남은금액 까지 랜덤
	        	// 5원 입력, 5인이면 각 1,1,1,1,1
	            money = (int)(Math.random() * (amount- cnt) + 1 );
	        }else{
	        	//남은금액
	            money = amount;
	        }
	        
	        
	        //토큰 생성후 사용중인 토큰이 나오면 다시 생성
	        Spread result = new Spread(); 
	        
	        //토큰생성, 중복시 다시 생성
	        if(token == null) {
	        	while(obj != null) {
	        		token = createToken(result);
	        		result.setToken(token);
	        		obj = repository.findToken(token );
	        	}
	        }
	        
	        //결과값 데이터세팅
	        result.setMoney(money);
	        result.setToken(token);
	        result.setRegId(regId);
	        result.setRoomId(roomId);
	        result.setRegDate(regDate);
	        result.setTotalAmount(data.getTotalAmount());
	        result.setTotalCnt(data.getTotalCnt());

	        //결과저장
        	repository.save(result);     
	        
	        result.setAmount(amount-money); //남은금액 - 수령금액
	        result.setCnt(cnt-1);			//수령인   -  1
	        
	        //재귀
	        calculator(result);
		}
		
	}
	
	//토큰생성 3자리, 대문자, 소문자, 숫자 조합
	public String createToken(Spread result) {
		
		StringBuffer token = new StringBuffer();
		
		//3회반복 
		for (int i = 0; i < 3; i++) {
			
			//0~2 랜덤
		    int rIndex = (int)(Math.random() * (3));
		    
		    // 1~3 자리수 마다 0이면 소문자1이면 대문자 2면숫자로 토큰값 조합
		    switch (rIndex) {
		    
		    case 0:
		        // 소문자; a-z
		        token.append((char)((int)(Math.random()*26)+97));
		        break;
		    case 1:
		        // 대문자; A-Z
		        token.append((char)((int)(Math.random()*26)+65));
		        break;
		    case 2:
		        // 숫자; 0-9
		        token.append((int)(Math.random() * (9) + 1 ));
		        break;
		    }
		}
		
		return token.toString();
	}

	@Override
	public Result secondProccess(Spread spread) {

		//데이터 세팅
		int id = spread.getReceiveId();
		String roomId = spread.getRoomId();
		String token = spread.getToken();
		StringBuffer msg = new StringBuffer();
		SecondResult result = new SecondResult();
		Errors errors  = new Errors();
		
		//에러발생 카운트
		int errorCnt = 0;
			
		//토큰있는지 조회 ; 뿌리기 시 발급된 token을 요청값으로 받습니다.
		Spread role1 = repository.findSecondRule1(roomId, token);
		if(role1== null){
			errors.setStatus("5XX");
			errors.setError("not found token");
			errors.setMessage("유효하지 않은 token입니다.");
			
			result.setSuccess(false);
			result.setErrors(errors);
			errorCnt++;
		}
		
		//10분 리미트 ;뿌린 건은 10분간만 유효합니다. 뿌린지 10분이 지난 요청에 대해서는 받기실패 응답이 내려가야 합니다.
		if(errorCnt == 0) {
			Spread role3 = repository.findSecondRule3(roomId, token);
			
			if(role3== null){
				errors.setStatus("5XX");
				errors.setError("Tokens out of validity time");
				errors.setMessage("유효시간이 지난 토큰입니다.");
				
				result.setSuccess(false);
				result.setErrors(errors);
				errorCnt++;
			}
		}
		
		//뿌리기 당 한 사용자는 한번만 받을 수 있습니다
		if(errorCnt == 0) {
			Spread role4 = repository.findSecondRule4(roomId, id, token);
			
			if(role4 == null){
				errors.setStatus("5XX");
				errors.setError("You have already received");
				errors.setMessage("뿌리기 당 한 사용자는 한번만 받을 수 있습니다.");
				
				result.setSuccess(false);
				result.setErrors(errors);
				errorCnt++;
			}
		}
		
		// 자신이등록한건 아닌지;  자신이 뿌리기한 건은 자신이 받을 수 없습니다.
		if(errorCnt == 0) {
			Spread role5 = repository.findSecondRule5(roomId, id,token);
			
			if(role5== null){
				errors.setStatus("5XX");
				errors.setError("You can't take your own.");
				errors.setMessage("자신이 뿌리기한 건은 자신이 받을 수 없습니다.");
				
				result.setSuccess(false);
				result.setErrors(errors);
				errorCnt++;
			}
		}
		
		// 같은방 인원인지 ; 뿌리기가 호출된 대화방과 동일한 대화방에 속한 사용자만이 받을 수있습니다.
		if(errorCnt == 0) {
			Spread role2 = repository.findSecondRule2(roomId, id, token);
			
			if(role2== null){
				errors.setStatus("5XX");
				errors.setError("not found token");
				errors.setMessage("유효하지 않은 token입니다.");
				
				result.setSuccess(false);
				result.setErrors(errors);
				errorCnt++;
			}
		}
		
			
		if(errorCnt == 0) {

			//뿌리기 건 중 아직 누구에게도 할당되지 않은 분배건 하나를 조회
			Spread data = repository.findMinCntByToken(roomId, token);
			
			
			//토큰을 모두 수령했거나, 시간이 초과한경우
			if(data== null){
				errors.setStatus("5XX");
				errors.setError("token is empty");
				errors.setMessage("받을 수 있는 토큰이 없습니다.");
				
				result.setSuccess(false);
				result.setErrors(errors);
				errorCnt++;
			}else {
				
				
				int cnt = data.getCnt();
				int amount = data.getMoney();
				
				//수령할 토큰의 분배건 SEQ 조회
				int resultCnt = repository.getToken(id, roomId, token, cnt);
				
				if(resultCnt != 0){
					
					//수령금액 입금처리
					repository.deposit(id, amount);
					
					
					result.setRoomId(roomId);
					result.setToken(token);
					result.setMoney(amount);
					
					
				}else {
					//수령할 토큰의 분배건 SEQ이 없는경우
					errors.setStatus("5XX");
					errors.setError("token is empty");
					errors.setMessage("받을 수 있는 토큰이 없습니다.");
					
					result.setSuccess(false);
					result.setErrors(errors);
					errorCnt++;
				}
			}
		}
		
		if(errorCnt== 0 && result.getSuccess() == null) {
			errors.setStatus("200");
			errors.setError("success");;
			
			result.setSuccess(true);
			result.setErrors(errors);
			
			/*
			 * {
				    "success": true,
				    "errors": {
				        "status": "200",
				        "error": "success",
				        "message": null
				    },
				    "roomId": "R0001",
				    "token": "3U5",
				    "money": 40
				}
			 * */
		}
		
		
		return result;
	}

	@Override
	public Result thirdProccess(Spread spread) {
		
		//데이터 세팅
		int id = spread.getReceiveId();
		String token = spread.getToken();

		StringBuffer msg = new StringBuffer();
		ThirdResult result = new ThirdResult();
		Errors errors  = new Errors();
		
		//에러카운트
		int errorCnt =0;
		
		
		
		// 뿌린 사람 자신만 조회를 할 수 있습니다. 다른사람의 뿌리기건이나 유효하지않은 token에 대해서는 조회 실패 응답이 내려가야 합니다.
		// 뿌린 건에 대한 조회는 7일 동안 할 수 있습니다.
		List<Spread> list = repository.findThirdRule1(id, token);
		if(list.size() == 0){
			errors.setStatus("5XX");
			errors.setError("not found token");
			errors.setMessage("유효하지 않은 token입니다.");
			
			result.setSuccess(false);
			result.setErrors(errors);
			errorCnt++;
		}else{
			
			String regDate = null;
			int spreadAmount = 0;
			int receivedAmount = 0;
	
			RecieveInfo receiveInfo = null;
			List<RecieveInfo> recieveList = new ArrayList<>();  
			
			//수령인 리스트 생성
			for (int i = 0; i < list.size(); i++) {
				Spread data = list.get(i);
				
				if(i==0){
					regDate = list.get(i).getRegDate();
					spreadAmount = list.get(i).getTotalAmount();
				}
				receivedAmount += data.getMoney();
				receiveInfo = new RecieveInfo();
				receiveInfo.setAmount(data.getMoney());
				receiveInfo.setReceieve_id(data.getReceiveId());
				recieveList.add(receiveInfo);
			}
			
			result.setRegDate(regDate);
			result.setSpreadAmount(spreadAmount);
			result.setReceivedAmount(receivedAmount);
			result.setReceivedList(recieveList);
		}
		
		
		//성공메시지 
		if(errorCnt== 0 && result.getSuccess() == null) {
			errors.setStatus("200");
			errors.setError("success");;
			
			result.setSuccess(true);
			result.setErrors(errors);
			
			/*
			 * {
				    "success": true,
				    "errors": {
				        "status": "200",
				        "error": "success",
				        "message": null
				    },
				    "regDate": "2020-11-23 21:49:34",
				    "spreadAmount": 2000,
				    "receivedAmount": 783,
				    "receivedList": [
				        {
				            "amount": 231,
				            "receieve_id": 2
				        },
				        {
				            "amount": 502,
				            "receieve_id": 3
				        },
				        {
				            "amount": 10,
				            "receieve_id": 4
				        },
				        {
				            "amount": 40,
				            "receieve_id": 5
				        }
				    ]
				}
			 * */
		}

		return result;
	}
	

}

