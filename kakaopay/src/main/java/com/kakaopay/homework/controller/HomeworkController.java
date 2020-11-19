package com.kakaopay.homework.controller;


import java.net.http.HttpRequest;
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
import com.kakaopay.homework.entity.Customer;
import com.kakaopay.homework.entity.Result;
import com.kakaopay.homework.entity.Spread;

import ch.qos.logback.classic.spi.PackagingDataCalculator;

@RestController
public class HomeworkController {
	private CustomerRepository repository;

	public HomeworkController(CustomerRepository repository) {
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
		
		Result result = new Result();
	 	
		Date today = new Date();
    	SimpleDateFormat sdf;
    	sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    	
		if(amount < cnt){
			result.setStatus("5XX");
			result.setError("more than Amount");
			result.setMessage("인원수 보다 많은 금액을 입력하세요.");
			
        }else {
        	
        	int id = Integer.parseInt(request.getHeader("X-USER-ID").toString());
        	String roomId = request.getHeader("X-ROOM-ID");
        	Spread data = new Spread();
        	data.setRegId(id);
        	data.setRoomId(roomId);
        	data.setAmount(amount);
        	
       
        	
        	data.setRegDate(sdf.format(today));
        	data.setCnt(cnt);
        	calculator(data);
        	
        }
		
		if(result.getStatus() == null) {
			result.setStatus("200");
			result.setMessage("성공");
		}
		 
		 return result;
		 
	}
	
	
	
	
	
	private void calculator(Spread data) {
		int amount = data.getAmount(); 	//입력금액
		int cnt = data.getCnt();					//수령인 숫자
		int money = 0;					//수령금액
		int regId = data.getRegId();
		String roomId = data.getRoomId();
		String regDate = data.getRegDate();
		
		
		if(cnt != 0){
            
            
            if(cnt !=1 ){
                money = (int)(Math.random() * (amount- cnt) + 1 ) ;
            }else{
                money = amount;
            }
            
            //수령금액
            
            Spread result = new Spread(); 
            result.setMoney(money);
            result.setToken("aaa");
            result.setAmount(amount-money);
            result.setCnt(cnt-1);
            result.setRegId(regId);
            result.setRoomId(roomId);
            result.setRegDate(regDate);
            
            result.setToken(createToken(result));
            
            System.out.println(result.toString());
            
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

	@PutMapping("/customer")
	public Customer putCustomer(Customer customer) {
		
		return repository.save(customer);
		
	}
	
	@PostMapping("/customer")
	public Customer postCustomer(Customer customer) {
		
		return repository.save(customer);
		
	}
	
	@DeleteMapping("/customer")
	public void deleteCustomer(int id) {
		
		repository.deleteById(id);
		
	}
	
	@GetMapping("/customer")
	public Customer getCustomer(int id) {
		
		return repository.findById(id).orElse(null);
		
	}
	
	@GetMapping("/customer/list")
	public List<Customer> allCustomer() {
		
		return (List<Customer>) repository.findAll();
		
	}
	
	@GetMapping("/customer/name")
    public List<Customer> getCustomer(String name) {
		
		return repository.findByName(name);
		
	}
	
	@GetMapping("/customer/search")
	public List<Customer> searchCustomer(String name) {
		
		return repository.findByNameLike("%"+name+"%");
		
	}
	
	@GetMapping("/customer/vip/list")
	public List<Customer> vipCustomer(String value1, String value2) {
		
		return (List<Customer>) repository.findVipList(value1, value2);
				
		
	}
	
	@GetMapping("/customer/vip2/list")
	public List<Customer> vip2Customer(String value1, String value2) {
		
		return (List<Customer>) repository.findVipList(value1, value2);
		
		
	}
	
		
	
	
}
