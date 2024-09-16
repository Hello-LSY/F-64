![image](https://github.com/Hello-LSY/F-64/assets/81401604/19c9b6da-ff9c-4a9f-9422-bc4f1fd172e8)


# EM - 사진동아리 F-64를 위한 사이트

대학교에 입학하고 졸업할때까지 사진동아리에 몸담았었는데 딱히 커뮤니티가 없어서 있었으면 더 좋지 않았을까? 하는 생각으로 진행한 프로젝트입니다 😀 

## 업데이트 내역

23-04-10 로그인과 자유게시판 추가

23-04-14 자유게시판 CRUD 추가, 댓글기능 추가

23-04-17 동아리 행사일정 개발

23-05-02 동아리 행사일정 추가

23-05-10 출사지 신청 기능 추가

23-05-23 네비게이션바 추가, QnA 게시판 추가 

23-11-23 AI 이미지 업스케일링 기능 추가

24-03-05 전반적인 오류 수정

## 기술 스택

- Spring
- Flask(AI 업스케일링)
- Bootstrap/Thymeleaf
- Maria DB

## ERD 및 구성도

아키텍처

![f64 (1)](https://github.com/user-attachments/assets/32510fea-5694-427f-a5b2-9a63f126b94a)


웹 페이지 

<img width="50%" src="https://github.com/Hello-LSY/F-64/assets/81401604/0dd51107-cf9f-4633-820e-463f90701f37">

new) 이미지 AI 업스케일링 구성도

![image](https://github.com/Hello-LSY/F-64/assets/81401604/e065e453-0cf9-4b94-b893-ddb98ab965fd)


## 기능

### 로그인/회원가입

<img width="50%" src="https://github.com/Hello-LSY/F-64/assets/81401604/fb0b7bf2-e1ae-4122-a9a4-a543ab9cec5a"/>

### 자유게시판

<img width="50%" src="https://github.com/Hello-LSY/F-64/assets/81401604/b7739087-ad8b-4852-9ee8-b67a445f5435"/>

- CRUD 가능
- 댓글 기능
- 사진 첨부
- 조회수 기능
- 추천 기능(동일 게시글 추천 중복안됨)

### 문의 게시판

<img width="50%" src="https://github.com/Hello-LSY/F-64/assets/81401604/bd2e35db-47c9-4a14-b452-f6fce75a1858"/>

- CRD 가능
- 익명, 비밀글 토글
- 익명 : admin 권한과 본인 계정을 제외하고 이름은 익명처리
- 비밀글 : admin 권한과 본인 계정을 제외하고 게시글 접근 불가
- 답변 기능/ 문의 상태

### 출사 신청 게시판

<img width="50%" src="https://github.com/Hello-LSY/F-64/assets/81401604/0201725e-cb13-425f-9107-b8a6a03d8ec8"/>

- kakaoMap API 사용
- CRD 가능

### 행사 일정

<img width="50%" src="https://github.com/Hello-LSY/F-64/assets/81401604/08cbfdc5-6765-40f7-949c-419f6fea327f"/>

- Fullcalendar 라이브러리 사용
- 월/일 로 스케줄 CRD 가능
- admin 권한 필요

---

### new) 이미지 AI 업스케일링 기능 

- Real-Esrgan 사용
- 업스케일링 처리를 위해 spring - flask 연동

#### 기본 기능

<img width="50%" src="https://github.com/Hello-LSY/F-64/assets/81401604/6a7c48e2-6faa-45c9-be3b-7cbe2bbb4165"/>

#### 얼굴 개선 기능
<img width="50%" src="https://github.com/Hello-LSY/F-64/assets/81401604/ec2b01f9-2431-4d06-82e5-551be020691e"/>

- gfggan 사용

저장된 사진 리포지토리

<img src="https://github.com/Hello-LSY/F-64/assets/81401604/1b7e6d58-a68b-4dce-a878-9ff1b7d13f9c">

#### 업스케일링 평가

- RTX 3070
- 5600X

애니메이션 이미지

![image](https://github.com/Hello-LSY/F-64/assets/81401604/f7bbb607-a965-4923-9550-3ade92da3ea5)

사람 이미지

![image](https://github.com/Hello-LSY/F-64/assets/81401604/86e4c7a1-c037-416b-afcc-30b93a424226)

배경 및 일반 이미지

![image](https://github.com/Hello-LSY/F-64/assets/81401604/0339243c-e370-48ab-a641-f0b77a4a2422)



