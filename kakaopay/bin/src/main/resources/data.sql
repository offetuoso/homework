insert into customer(id, name, amount) values(1, '라이언',10000000);
insert into customer(id, name, amount) values(2, '무지',10000000);
insert into customer(id, name, amount) values(3, '제이지',10000000);
insert into customer(id, name, amount) values(4, '콘',10000000);
insert into customer(id, name, amount) values(5, '튜브',10000000);
insert into customer(id, name, amount) values(6, '어피치',10000000);
insert into customer(id, name, amount) values(7, '춘식이',10000000);


insert into room(room_id, room_Name) values('R0001','카카오프렌즈');


insert into member(room_id, id, nick_name) values('R0001', 1, '팔불출 라이언');
insert into member(room_id, id, nick_name) values('R0001', 2, '무지 무지');
insert into member(room_id, id, nick_name) values('R0001', 3, '폭탄머리 제이지');
insert into member(room_id, id, nick_name) values('R0001', 4, '옥수수 콘');
insert into member(room_id, id, nick_name) values('R0001', 5, '튜브장인 튜브');
insert into member(room_id, id, nick_name) values('R0001', 6, '어차피 어피치');
insert into member(room_id, id, nick_name) values('R0001', 7, '막내 춘식이');



/*


select S.ROOM_ID
        , R.ROOM_NAME
        , S.TOKEN , S.MONEY
        , S.REG_ID
        , M.NICK_NAME
        , S.REG_DATE 


 from SPREAD S
INNER JOIN ROOM R ON (S.ROOM_ID  = R.ROOM_ID )
INNER JOIN MEMBER M ON ( M.room_id = R.ROOM_ID AND M.ID = S.REG_ID )

          where S.room_id = 'R0001'
                       and S.reg_id = 1
                   
                   

insert into SPREAD (ROOM_ID ,TOKEN ,MONEY ,REG_ID ,REG_DATE ) values ('R00001', 'oj4', 79, 1, 20201120012707)


--룸, 멤버, 유저 

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