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

	
	
	@Query(value="select S.SEQ			"
			+ "        , S.ROOM_ID		"
			+ "        , R.ROOM_NAME	"
			+ "        , S.TOKEN "
			+ "        , S.MONEY		"
			+ "        , S.REG_ID		"
			+ "        , M.NICK_NAME	"
			+ "        , S.REG_DATE 	"
			+ "        , S.TOTAL_AMOUNT "
			+ "        , S.TOTAL_CNT	"
			+ " from SPREAD S			"
			+ "JOIN ROOM R ON (S.ROOM_ID  = R.ROOM_ID )		"
			+ "JOIN MEMBER M ON ( M.room_id = R.ROOM_ID AND M.ID = S.REG_ID )	"
			+ "\n"
			+ "          where S.room_id = :roomId	"
			+ "                       and S.reg_id = :id	"
			+ " and to_char(DATEADD(MINUTE, +10, to_date(reg_date, 'YYYYMMDDHH24MISS') ) , 'YYYYMMDDHH24MISS')  >  to_char(sysdate, 'YYYYMMDDHH24MISS')	"
			+ "and REG_DATE = (select MAX(reg_date) from SPREAD 	"
			+ "                 where room_id = :roomId	"
			+ "                   and reg_id = :id	"
			+ "                 )", nativeQuery=true)
	List<Spread> findCreatedSpreadList(String roomId, int id);



	@Query(value="SELECT * "
			+ "  FROM SPREAD "
			+ " where ROOM_ID = :roomId "
			+ "   AND TOKEN = :token "
			+ "  ", nativeQuery=true)
	Spread findSecondRule1(String roomId, String token);

	@Query(value="SELECT * "
			+ "     FROM SPREAD "
			+ "    where ROOM_ID = :roomId"
			+ "	     AND TOKEN = :token"
			+ "      AND RECEIV_ID = 0"
			+ "      AND RECIV_DATE IS NULL"
			+ "  ", nativeQuery=true)
	Spread findSecondRule2(String roomId, String token);
	
	
	
	@Query(value="SELECT * \n"
			+ "  FROM SPREAD \n"
			+ " where ROOM_ID = :roomId\n"
			+ "   AND TOKEN = :token\n"
			+ "   and to_char(DATEADD(MINUTE, +10, to_date(reg_date, 'YYYYMMDDHH24MISS') ) , 'YYYYMMDDHH24MISS')  >  to_char(sysdate, 'YYYYMMDDHH24MISS')\n"
			+ "   and reg_id <> :id\n"
			+ "   and 0 = (\n"
			+ "   			SELECT COUNT(1) \n"
			+ "   			  FROM SPREAD \n"
			+ "             where REG_DATE = :regDate \n"
			+ "	           AND ROOM_ID = :roomId \n"
			+ "               AND RECEIV_ID = :id \n"
			+ "           ) "
			+ "   AND 0 < ("
			+ "             select count(1)  from  MEMBER \n"
			+ "              where ROOM_ID = :roomId\n"
			+ "                and id = :id)\n"
			+ ""
			+ "  ", nativeQuery=true)
	Spread findMoneyByToken(String roomId, int id, String token, String regDate);



}


