insert into customer(id, name, amount) values(1, '라이언',100000000);
insert into customer(id, name, amount) values(2, '무지',100000000);
insert into customer(id, name, amount) values(3, '제이지',100000000);
insert into customer(id, name, amount) values(4, '콘',100000000);
insert into customer(id, name, amount) values(5, '튜브',100000000);
insert into customer(id, name, amount) values(6, '어피치',100000000);
insert into customer(id, name, amount) values(7, '춘식이',100000000);
insert into customer(id, name, amount) values(8, '피카츄',100000000);
insert into customer(id, name, amount) values(9, '이상해씨',100000000);
insert into customer(id, name, amount) values(10, '파이리',100000000);
insert into customer(id, name, amount) values(11, '꼬북이',100000000);


insert into room(room_id, room_Name) values('R0001','카카오프렌즈');
insert into room(room_id, room_Name) values('R0002','포켓몬');


insert into member(room_id, id, nick_name) values('R0001', 1, '팔불출 라이언');
insert into member(room_id, id, nick_name) values('R0001', 2, '무지 무지');
insert into member(room_id, id, nick_name) values('R0001', 3, '폭탄머리 제이지');
insert into member(room_id, id, nick_name) values('R0001', 4, '옥수수 콘');
insert into member(room_id, id, nick_name) values('R0001', 5, '튜브장인 튜브');
insert into member(room_id, id, nick_name) values('R0001', 6, '어차피 어피치');
insert into member(room_id, id, nick_name) values('R0001', 7, '막내 춘식이');

insert into member(room_id, id, nick_name) values('R0002', 1, '피카츄');
insert into member(room_id, id, nick_name) values('R0002', 2, '이상해씨');
insert into member(room_id, id, nick_name) values('R0002', 3, '파이리');
insert into member(room_id, id, nick_name) values('R0002', 4, '꼬북이');


/*



SELECT 
R.ROOM_NAME
, S.REG_ID
, S.REG_DATE
, S.TOKEN

  FROM SPREAD S
INNER JOIN ROOM R ON (S.ROOM_ID = R.ROOM_ID )
where S.room_id = 'R0001'
and S.reg_id= 1
and S.reg_date = (
SELECT MAX(REG_DATE) LAST FROM SPREAD 
where room_id = 'R0001'
and reg_id= 1
group by ROOM_ID ,REG_ID
);












insert into SPREAD (TOKEN ,MONEY ,REG_DATE ,REG_ID ,ROOM_ID )
values('qA1' , 500  ,to_char(sysdate, 'YYYYMMDDHH24MISS'),1,'R00001' ); //뿌리기 테스트용 더미데이터ㅌ




select * from SPREAD 
where room_id = 'R00001'
and token =  'qA1'
and to_char(DATEADD(MINUTE, +10, to_date(reg_date, 'YYYYMMDDHH24MISS') ) , 'YYYYMMDDHH24MISS')  >  to_char(sysdate, 'YYYYMMDDHH24MISS')


DATEADD(MINUTE, -10, SYSDATE) , 'YYYYMMDD HH24:MI:SS'), 10분





insert into SPREAD (TOKEN ,AMOUNT ,CNT ,MONEY ,RECEIV_ID ,RECIV_DATE ,REG_DATE ,REG_ID ,ROOM_ID, AMOUNT )
values('qA3' , 2000 , 3 ,500 , 2 , '' ,'20201120123500',1,'R00001' , 0 ,  )


--룸, 멤버, 유저 


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

SELECT R.ROOM_ID AS X_ROOM_ID
             , R.ROOM_NAME AS X_ROOM_ID
             , C.ID AS X_USER_ID
             , M.NICK_NAME   AS  X_NICK_NAME
             , C.AMOUNT AS X_AMOUNT
   FROM ROOM R
  INNER JOIN MEMBER M ON (R.ROOM_ID = M.ROOM_ID )
  INNER JOIN CUSTOMER C ON (C.ID = M.ID)



--토큰 값 생성 

StringBuffer temp = new StringBuffer();
Random rnd = new Random();
for (int i = 0; i < 20; i++) {
    int rIndex = rnd.nextInt(3);
    switch (rIndex) {
    case 0:
        // a-z
        temp.append((char) ((int) (rnd.nextInt(26)) + 97));
        break;
    case 1:
        // A-Z
        temp.append((char) ((int) (rnd.nextInt(26)) + 65));
        break;
    case 2:
        // 0-9
        temp.append((rnd.nextInt(10)));
        break;
    }
}


재귀 
public class Spread{
     
     public static void main(String []args){
       calc(100, 5);
     }
     
     public static void calc(int amount, int cnt){
      
        if(cnt != 0){
                 
                    int money =0;
                    
                    if(cnt !=1 ){
                        money = (int)(Math.random() * (amount- cnt) + 1 ) ;
                    }else{
                        money = amount;
                    }
                    
                    System.out.println("받은금액 "+money);
                 
                  calc(amount-money, cnt-1);
        }else if(amount < cnt){
			System.out.println("인원수 보다 많은 금액을 입력하세요.");
        }
     }
}






*/