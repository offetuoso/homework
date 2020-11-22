package com.kakaopay.homework.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.RollbackException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kakaopay.homework.dao.HomeworkRepository;
import com.kakaopay.homework.entity.Result;
import com.kakaopay.homework.entity.Spread;

@Service("HomeworkService")
public class HomeworkServiceImpl implements HomeworkService{
	@Autowired
	private HomeworkRepository repository;
	
	public Result firstProccess(Spread spread) {
		Result result = new Result();
		StringBuffer msg = new StringBuffer();
		
		String roomId = spread.getRoomId();
		int id = spread.getRegId();
		String token = "";
		String idxs = "";
		int maxMoney = 0;
		int totalAmount = spread.getTotalAmount();
		int totalCnt = spread.getTotalCnt();
		spread.setAmount(totalAmount);
		spread.setCnt(totalCnt);
		
		calculator(spread);
		
    	List<Spread> list = repository.findCreatedSpreadList(roomId, id);
    	
    	if(totalAmount < totalCnt){
			result.setStatus("5XX");
			result.setError("more than Amount");
			result.setMessage("인원수 보다 많은 금액을 입력하세요.");
			
        }else {
        	if(list != null) {
        		
        		//출금
        		repository.withdrawal(id, totalAmount);
        		
        		for (int i = 0; i < list.size(); i++) {
        			Spread data = list.get(i);
        			
        			if(!idxs.equals("")) idxs+=",";
        			idxs += ""+data.getCnt()+"";
        			
        			if(maxMoney < data.getMoney() )
        				maxMoney = data.getMoney();
        			
        			if(i==0)
        				token = data.getToken();
        		}
        		msg.append("'roomId':'"+roomId+"',");
        		msg.append("'regId':'"+id+"',");
        		msg.append("'amount':'"+totalAmount+"',");
        		msg.append("'maxMoney':'"+maxMoney+"',");
        		msg.append("'personCnt':'"+totalCnt+"',");
        		msg.append("'token':'"+token+"',");
        		msg.append("'idxs':["+idxs+"]");
        		
        		result.setStatus("200");
        		result.setError("success");
    			result.setMessage(msg.toString());
    			
        		
        	}else {
        		result.setStatus("5XX");
        		result.setError("not found result.");
        		result.setMessage("결과를 찾을 수 없습니다.");
        	}
        }
    	
    	return result;
	}

	public void calculator(Spread data) {
		int amount = data.getAmount(); 	//입력금액
		int cnt = data.getCnt();		//수령인 숫자
		int money = 0;					//수령금액
		int regId = data.getRegId();
		String roomId = data.getRoomId();
		String regDate = data.getRegDate();
		String token = data.getToken();
		Spread obj = new Spread();
		
		if(cnt != 0){
	        
			/*
	         * 		수령금액 생성 
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
	        
	        if(token == null) {
	        	while(obj != null) {
	        		token = createToken(result);
	        		result.setToken(token);
	        		
	        		obj = repository.findToken(token );
	        		
	        	}
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
	
	public String createToken(Spread result) {
		String roomId = result.getRoomId();
		
		StringBuffer token = new StringBuffer();
		for (int i = 0; i < 3; i++) {
		    int rIndex = (int)(Math.random() * (3));
		    switch (rIndex) {
		    case 0:
		        // a-z
		        token.append((char)((int)(Math.random()*26)+97));
		        break;
		    case 1:
		        // A-Z
		        token.append((char)((int)(Math.random()*26)+65));
		        break;
		    case 2:
		        // 0-9
		        token.append((int)(Math.random() * (9) + 1 ));
		        break;
		    }
		}
		
		return token.toString();
	}

	@Override
	public Result secondProccess(Spread spread) {
		
		int id = spread.getReceiveId();
		String roomId = spread.getRoomId();
		String token = spread.getToken();
		Result result = new Result();
		StringBuffer msg = new StringBuffer();
		
		int errorCnt = 0;

			
		//토큰있는지 조회 ; 뿌리기 시 발급된 token을 요청값으로 받습니다.
		Spread role1 = repository.findSecondRule1(roomId, token);
		
		if(role1== null){
			result.setStatus("5XX");
			result.setError("not found token");
			result.setMessage("유효하지 않은 token입니다.");
			errorCnt++;
		}
		
		//10분 리미트 ;뿌린 건은 10분간만 유효합니다. 뿌린지 10분이 지난 요청에 대해서는 받기실패 응답이 내려가야 합니다.
		if(errorCnt == 0) {
			Spread role3 = repository.findSecondRule3(roomId, token);
			
			if(role3== null){
				result.setStatus("5XX");
				result.setError("Tokens out of validity time");
				result.setMessage("유효시간이 지난 토큰입니다.");
				errorCnt++;
			}
		}
		
		//뿌리기 당 한 사용자는 한번만 받을 수 있습니다
		if(errorCnt == 0) {
			Spread role4 = repository.findSecondRule4(roomId, id, token);
			
			if(role4 == null){
				result.setStatus("5XX");
				result.setError("You have already received");
				result.setMessage("뿌리기 당 한 사용자는 한번만 받을 수 있습니다.");
				errorCnt++;
			}
		}
		
		// 자신이등록한건 아닌지;  자신이 뿌리기한 건은 자신이 받을 수 없습니다.
		if(errorCnt == 0) {
			Spread role5 = repository.findSecondRule5(roomId, id,token);
			
			if(role5== null){
				result.setStatus("5XX");
				result.setError("You can't take your own.");
				result.setMessage("자신이 뿌리기한 건은 자신이 받을 수 없습니다.");
				errorCnt++;
			}
		}
		
		// 같은방 인원인지 ; 뿌리기가 호출된 대화방과 동일한 대화방에 속한 사용자만이 받을 수있습니다.
		if(errorCnt == 0) {
			Spread role2 = repository.findSecondRule2(roomId, id, token);
			
			if(role2== null){
				result.setStatus("5XX");
				result.setError("not found token");
				result.setMessage("유효하지 않은 token입니다.");
				errorCnt++;
			}
		}
		
			
		
		if(errorCnt == 0) {
			Spread data = repository.findMinCntByToken(roomId, token);
			
			if(data== null){
				result.setStatus("5XX");
				result.setError("token is empty");
				result.setMessage("받을 수 있는 토큰이 없습니다.");
				errorCnt++;
			}else {
				int cnt = data.getCnt();
				int amount = data.getMoney();
				int resultCnt = repository.getToken(id, roomId, token, cnt);
				
				if(resultCnt != 0){
					repository.deposit(id, amount);
					
					msg.append("'roomId':'"+roomId+"',");		
					msg.append("'token':'"+token+"',");		
					msg.append("'money':'"+amount+"',");		
					msg.append("'roomId':'"+roomId+"'");		
					
					
				}else {
					result.setStatus("5XX");
					result.setError("token is empty");
					result.setMessage("받을 수 있는 토큰이 없습니다.");
					errorCnt++;
				}
			}
		}
		
		if(errorCnt== 0 && result.getStatus() == null) {
			result.setStatus("200");
			result.setError("success");;
			result.setMessage(msg.toString());
		}
		
		
		return result;
	}

	@Override
	public Result thirdProccess(Spread spread) {

		int id = spread.getReceiveId();
		String roomId = spread.getRoomId();
		String token = spread.getToken();
		Result result = new Result();
		StringBuffer msg = new StringBuffer();
		
		int errorCnt =0;

		
			
		
		
		// 뿌린 사람 자신만 조회를 할 수 있습니다. 다른사람의 뿌리기건이나 유효하지않은 token에 대해서는 조회 실패 응답이 내려가야 합니다.
		// 뿌린 건에 대한 조회는 7일 동안 할 수 있습니다.
		List<Spread> list = repository.findThirdRule1(id, token);
		
		if(list.size() == 0){
			result.setStatus("5XX");
			result.setError("not found token");
			result.setMessage("유효하지 않은 token입니다.");
			errorCnt++;
		}else{
			String regDate = null;
			int spreadAmount = 0;
			int receivedAmount = 0;
	
			String receiveInfo = "";
			for (int i = 0; i < list.size(); i++) {
				Spread data = list.get(i);
				
				if(i==0){
					regDate = list.get(i).getRegDate();
					spreadAmount = list.get(i).getTotalAmount();
				}
				
				
				receivedAmount += data.getMoney();
				
				if(receiveInfo != "") {
					receiveInfo += ",";
				}
				receiveInfo+= "{\"amount\":"+data.getMoney()+",\"receieve_id\":\""+data.getReceiveId()+"\"}";
				
			}
			
			msg.append("'reg_date':'"+regDate+"',");	
			msg.append("'spread_amount':'"+spreadAmount+"',");
			msg.append("'received_amount':'"+receivedAmount+"',");	
			msg.append("'received_list':'["+receiveInfo+"']");	
			
		}
		
		
		
		if(errorCnt== 0 && result.getStatus() == null) {
			result.setStatus("200");
			result.setError("success");;
			result.setMessage(msg.toString());
		}

		return result;
	}
	

}

