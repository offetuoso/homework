package com.kakaopay.homework.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.kakaopay.homework.entity.Customer;
import com.kakaopay.homework.entity.Spread;


public interface HomeworkRepository extends CrudRepository<Spread, Integer> {

	/*
	 * 		토큰생성 중복확인 
	 * */
	@Query(value="select * from SPREAD "
			+ "	     where token = :token "
			+ "	       and to_char(DATEADD(MINUTE, +10, to_date(reg_date, 'YYYYMMDDHH24MISS') ) , 'YYYYMMDDHH24MISS')  >  to_char(sysdate, 'YYYYMMDDHH24MISS') ", nativeQuery=true)
	Spread findToken(@Param("token") String token);

	
	
	/*
	 * 		토큰생성된 리스트 조회 
	 * */
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
			+ "        , S.CNT	"
			+ " from SPREAD S			"
			+ "JOIN ROOM R ON (S.ROOM_ID  = R.ROOM_ID )		"
			+ "JOIN MEMBER M ON ( M.room_id = R.ROOM_ID AND M.ID = S.REG_ID )	"
			+ "          where S.room_id = :roomId	"
			+ "                       and S.reg_id = :id	"
			+ " and to_char(DATEADD(MINUTE, +1, to_date(reg_date, 'YYYYMMDDHH24MISS') ) , 'YYYYMMDDHH24MISS')  >  to_char(sysdate, 'YYYYMMDDHH24MISS')	"
			+ " and REG_DATE = (select MAX(reg_date) from SPREAD 	"
			+ "                 where room_id = :roomId	"
			+ "                   and reg_id = :id	"
			+ "                 )"
			+ "	ORDER BY S.CNT "
			+ "", nativeQuery=true)
	List<Spread> findCreatedSpreadList(@Param("roomId") String roomId, @Param("id") int id);


	
	
	// 과제2 시작
	
	
	//토큰있는지 조회 ; 뿌리기 시 발급된 token을 요청값으로 받습니다.
	@Query(value="SELECT * "
			+ "  FROM SPREAD "
			+ " where ROOM_ID = :roomId "
			+ "   AND TOKEN = :token "
			+ "   AND CNT = 0"
			+ "  ", nativeQuery=true)
	Spread findSecondRule1(@Param("roomId") String roomId, @Param("token") String token);

	//뿌리기가 호출된 대화방과 동일한 대화방에 속한 사용자만이 받을 수있습니다.
	@Query(value="SELECT * "
			+ "			  FROM SPREAD "
			+ "			 where ROOM_ID = :roomId"
			+ "			   AND TOKEN =  :token"
			+ "			   AND CNT= 0"
			+ "			   AND 0 < ("
			+ "			             select count(1)  from  MEMBER "
			+ "			              where ROOM_ID = :roomId"
			+ "			                and id = :id)"
			+ "", nativeQuery=true)
	Spread findSecondRule2(@Param("roomId") String roomId, @Param("id") int id, @Param("token") String token);

	
	//10분 리미트 ;뿌린 건은 10분간만 유효합니다. 뿌린지 10분이 지난 요청에 대해서는 받기실패 응답이 내려가야 합니다.
	@Query(value="SELECT * "
			+ "			  FROM SPREAD "
			+ "			 where ROOM_ID = :roomId"
			+ "			   AND TOKEN = :token"
			+ "			   AND CNT = 0"
			+ "			   AND TO_CHAR(DATEADD(MINUTE, +10, TO_DATE(REG_DATE, 'YYYYMMDDHH24MISS') ) , 'YYYYMMDDHH24MISS')  >  to_char(sysdate, 'YYYYMMDDHH24MISS')"
			+ "", nativeQuery=true)
	Spread findSecondRule3(@Param("roomId") String roomId, @Param("token") String token);


	//뿌리기 당 한 사용자는 한번만 받을 수 있습니다
	@Query(value="SELECT * "
			+ "			  FROM SPREAD "
			+ "			 WHERE ROOM_ID = :roomId"
			+ "			   AND TOKEN = :token"
			+ "			   AND CNT= 0"
			+ "			   AND 0 = ("
			+ "			   			SELECT COUNT(1) "
			+ "			   			  FROM SPREAD "
			+ "			             WHERE TOKEN = :token"
			+ "				           AND ROOM_ID = :roomId"
			+ "			               AND RECEIVE_ID = :id"
			+ "			           ) "
			+ "", nativeQuery=true)
	Spread findSecondRule4(@Param("roomId") String roomId, @Param("id") int id, @Param("token") String token);



	//자신이 뿌리기한 건은 자신이 받을 수 없습니다.
	@Query(value="SELECT * "
			+ "			  FROM SPREAD "
			+ "			 where ROOM_ID = :roomId"
			+ "			   AND TOKEN = :token"
			+ "			   AND CNT= 0"
			+ "			   AND REG_ID <> :id"
			+ "", nativeQuery=true)
	Spread findSecondRule5(@Param("roomId") String roomId, @Param("id") int id, @Param("token") String token);

	
	//token에 해당하는 뿌리기 건 중 아직 누구에게도 할당되지 않은 분배건 하나 검색
	@Query(value="SELECT * "
			+ "     FROM SPREAD "
			+ "    WHERE ROOM_ID = :roomId"
			+ "      AND TOKEN = :token"
			+ "      AND CNT = ("
			+ "						SELECT MIN(CNT) AS CNT FROM SPREAD "
			+ "						 WHERE ROOM_ID = :roomId"
			+ "						   AND TOKEN = :token"
			+ "						   AND TO_CHAR(DATEADD(MINUTE, +10, TO_DATE(REG_DATE, 'YYYYMMDDHH24MISS') ) , 'YYYYMMDDHH24MISS')  >  to_char(sysdate, 'YYYYMMDDHH24MISS')"
			+ "						   AND RECEIVE_DATE IS NULL"
			+ "                        AND RECEIVE_ID = 0"
			+ "                )"
			+ "", nativeQuery=true)
	Spread findMinCntByToken(@Param("roomId") String roomId, @Param("token") String token);
	
	@Transactional
	@Modifying
	@Query(value = ""
			+ "UPDATE SPREAD										 		"
			+ "   SET RECEIVE_ID  =:id										"
			+ "     , RECEIVE_DATE = TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS')	"
			+ " WHERE ROOM_ID  = :roomId									"
			+ "   AND TOKEN = :token										"
			+ "   AND CNT = :cnt							   				"
			+ "", nativeQuery=true) 
	int getToken(@Param("id")int id, @Param("roomId")String roomId, @Param("token")String token, @Param("cnt")int cnt);
	
	
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE CUSTOMER c  							"
			+ "        set c.AMOUNT = c.AMOUNT - :amount 		"
			+ "      WHERE c.ID = :id							"
			+ "", nativeQuery=true)
	void withdrawal(@Param("id")int id, @Param("amount")int amount);
	
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE CUSTOMER c  							"
			+ "        set c.AMOUNT = c.AMOUNT + :amount 		"
			+ "      WHERE c.ID = :id							"
			+ "", nativeQuery=true)
	void deposit(@Param("id")int id, @Param("amount")int amount);



	//token에 해당하는 뿌리기 건 중 아직 누구에게도 할당되지 않은 분배건 하나 검색
	@Query(value="SELECT TO_CHAR(TO_DATE(REG_DATE, 'YYYYMMDDHH24MISS' ) , 'YYYY-MM-DD HH24:MI:SS') as REG_DATE										"
			+ "        , S.* 																														"
			+ "     FROM SPREAD S 																														"
			+ "    WHERE TOKEN = :token 																												"
			+ "      AND REG_ID = :id 																													"
			+ "      AND TO_CHAR(DATEADD(DAY, +7, TO_DATE(REG_DATE, 'YYYYMMDDHH24MISS') ) , 'YYYYMMDDHH24MISS')  >  to_char(sysdate, 'YYYYMMDDHH24MISS')	"
			+ "      AND RECEIVE_DATE IS NOT NULL"
			+ "", nativeQuery=true)
	List<Spread> findThirdRule1(int id, String token);



}


