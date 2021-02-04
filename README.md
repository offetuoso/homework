# Project Title / 카카오페이 뿌리기 Back-end 기능 구현  

** 카카오페이  **  


Introduction


카카오페이에는 머니 뿌리기 기능이 있습니다.


● 사용자는 다수의 친구들이 있는 대화방에서 뿌릴 금액과 받아갈 대상의 숫자를 입력하여 뿌리기 요청을 보낼 수 있습니다.


● 요청 시 자신의 잔액이 감소되고 대화방에는 뿌리기 메세지가 발송됩니다.


● 대화방에 있는 다른 사용자들은 위에 발송된 메세지를 클릭하여 금액을 무작위로 받아가게 됩니다.



이번 과제에서는 UI 및 메세지 영역은 제외한 간소화된 REST API를 구현하는 것이


목표입니다.

## 개발 환경

기본 환경

IDE: sts-4.8.1.RELEASE 

OS: Mac OS X, WINDOWS 10

GIT

Server

Java8

Spring Boot 2.4.0

JPA

H2

Junit5


## Getting Started / 어떻게 시작하나요?

깃 저장소 : https://github.com/offetuoso/homework.git

구글드라이브 : https://drive.google.com/file/d/1KVk6UPnQh8xJup4mCo_xaLOeqTea3xLW/view?usp=sharing


### Prerequisites / 선행 조건


H2 DB 사용으로 기동시 Entity 생성과 인서트쿼리 실행

    Customer.java
    
    Member.java
    
    Room.java
    
    Spread.java
    



 data.sql   --초기 해당쿼리 실행 

// 유저 생성

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


// 방생성

insert into room(room_id, room_Name) values('R0001','카카오프렌즈');

insert into room(room_id, room_Name) values('R0002','포켓몬');


//방 멤버 추가

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



### 요구 사항
● 뿌리기, 받기, 조회 기능을 수행하는 REST API 를 구현합니다.

○ 요청한 사용자의 식별값은 숫자 형태이며 "X-USER-ID" 라는 HTTP Header로
전달됩니다.

○ 요청한 사용자가 속한 대화방의 식별값은 문자 형태이며 "X-ROOM-ID" 라는
HTTP Header로 전달됩니다.

○ 모든 사용자는 뿌리기에 충분한 잔액을 보유하고 있다고 가정하여 별도로
잔액에 관련된 체크는 하지 않습니다.

● 작성하신 어플리케이션이 다수의 서버에 다수의 인스턴스로 동작하더라도 기능에
문제가 없도록 설계되어야 합니다.

● 각 기능 및 제약사항에 대한 단위테스트를 반드시 작성합니다.


### 상세 구현 요건 및 제약사항


1. 뿌리기 API

● 다음 조건을 만족하는 뿌리기 API를 만들어 주세요.

○ 뿌릴 금액, 뿌릴 인원을 요청값으로 받습니다.

○ 뿌리기 요청건에 대한 고유 token을 발급하고 응답값으로 내려줍니다.

○ 뿌릴 금액을 인원수에 맞게 분배하여 저장합니다. (분배 로직은 자유롭게
구현해 주세요.)

○ token은 3자리 문자열로 구성되며 예측이 불가능해야 합니다.


2. 받기 API

● 다음 조건을 만족하는 받기 API를 만들어 주세요.

○ 뿌리기 시 발급된 token을 요청값으로 받습니다.

○ token에 해당하는 뿌리기 건 중 아직 누구에게도 할당되지 않은 분배건 하나를

API를 호출한 사용자에게 할당하고, 그 금액을 응답값으로 내려줍니다.

○ 뿌리기 당 한 사용자는 한번만 받을 수 있습니다.

○ 자신이 뿌리기한 건은 자신이 받을 수 없습니다.

○ 뿌린기가 호출된 대화방과 동일한 대화방에 속한 사용자만이 받을 수
있습니다.

○ 뿌린 건은 10분간만 유효합니다. 뿌린지 10분이 지난 요청에 대해서는 받기
실패 응답이 내려가야 합니다.


3. 조회 API

● 다음 조건을 만족하는 조회 API를 만들어 주세요.

○ 뿌리기 시 발급된 token을 요청값으로 받습니다.

○ token에 해당하는 뿌리기 건의 현재 상태를 응답값으로 내려줍니다. 현재
상태는 다음의 정보를 포함합니다.

○ 뿌린 시각, 뿌린 금액, 받기 완료된 금액, 받기 완료된 정보 ([받은 금액, 받은
사용자 아이디] 리스트)

○ 뿌린 사람 자신만 조회를 할 수 있습니다. 다른사람의 뿌리기건이나 유효하지
않은 token에 대해서는 조회 실패 응답이 내려가야 합니다.

○ 뿌린 건에 대한 조회는 7일 동안 할 수 있습니다.


