package com.kakaopay.homework.service;

import java.util.List;

import com.kakaopay.homework.entity.Result;
import com.kakaopay.homework.entity.Spread;

public interface HomeworkService {

	// 뿌리기 기능
	public Result firstProccess(Spread spread);
	
	//받기 기능
	public Result secondProccess(Spread spread);
	
	//조회 기능
	public Result thirdProccess(Spread spread);
	
	// 돈 분배 기능 
	public void calculator(Spread data);
	
	//토큰생성
	public String createToken(Spread result);
}