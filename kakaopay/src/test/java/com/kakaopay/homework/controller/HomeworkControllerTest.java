package com.kakaopay.homework.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kakaopay.homework.entity.Spread;
import com.kakaopay.homework.service.HomeworkServiceImpl;
import com.kakaopay.homework.vo.Errors;
import com.kakaopay.homework.vo.FirstResult;
import com.kakaopay.homework.vo.Result;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HomeworkControllerTest {
	
	@Autowired
	HomeworkServiceImpl service;
	
    @Test
       public void spread() {
        
    
    	
    	
		int id = 1;
		String roomId = "R0001";
		int amount = 2000;
		int cnt = 5;
		
		Date today = new Date();
    	SimpleDateFormat sdf;
    	sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    	
    	Spread data = new Spread();
    	data.setRegId(id);				
    	data.setRegDate(sdf.format(today));	//토큰별 동일한 시간 입력
    	data.setRoomId(roomId);
    	data.setTotalAmount(amount);
    	data.setTotalCnt(cnt);
		 
    	service.firstProccess(data);
        	
        
    }
}