package com.kakaopay.homework.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.kakaopay.homework.entity.Customer;
import com.kakaopay.homework.entity.Spread;


public interface SpreadRepository extends CrudRepository<Spread, Integer> {

	/*
	 * 		토큰생성 중복확인 
	 * */
	@Query(value="select * from SPREAD "
			+ "	     where room_id = :roomId"
			+ "	       and token = :token "
			+ "	       and to_char(DATEADD(MINUTE, +10, to_date(reg_date, 'YYYYMMDDHH24MISS') ) , 'YYYYMMDDHH24MISS')  >  to_char(sysdate, 'YYYYMMDDHH24MISS') ", nativeQuery=true)
	Spread findToken(@Param("roomId") String roomId, @Param("token") String token);

	
	
	@Query(value="select * from SPREAD "
			+ "	     where room_id = :roomId"
			+ "	       and token = :id "
			+ "	       and to_char(DATEADD(MINUTE, +10, to_date(reg_date, 'YYYYMMDDHH24MISS') ) , 'YYYYMMDDHH24MISS')  >  to_char(sysdate, 'YYYYMMDDHH24MISS') ", nativeQuery=true)
	List<Spread> findCreatedSpreadList(String roomId, int id);
	
	
}


