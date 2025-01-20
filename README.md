# EM - 사진동아리 F-64를 위한 사이트
![f64 (2)](https://github.com/user-attachments/assets/80808c9b-3923-47d6-85fc-80b0e107dfcd)

![image](https://github.com/user-attachments/assets/e5a942fb-546f-4fa4-8388-1c9c95c4a408)


대학교에 입학하고 졸업할때까지 사진동아리에 몸담았었는데 플랫폼이 없어서 있었으면 더 좋지 않았을까? 하는 생각으로 진행한 프로젝트입니다 😀 

## 업데이트 내역

23-04-10 로그인과 자유게시판 추가

23-04-14 자유게시판 CRUD 추가, 댓글기능 추가

23-04-17 동아리 행사일정 개발

23-05-02 동아리 행사일정 추가

23-05-10 출사지 신청 기능 추가

23-05-23 네비게이션바 추가, QnA 게시판 추가 

23-11-23 AI 이미지 업스케일링 기능 추가

24-03-05 전반적인 오류 수정

25-01-20 프로젝트 전체 리팩토링

## 기술 스택

- Spring(boot)
- Flask(AI 업스케일링) -> 스프링 자체 + 가상환경 이용
- Bootstrap/Thymeleaf -> tailwind css/Thymeleaf
- Maria DB -> mysql 

## ERD 및 구성도

웹 페이지 

<img width="50%" src="https://github.com/Hello-LSY/F-64/assets/81401604/0dd51107-cf9f-4633-820e-463f90701f37">

new) 이미지 AI 업스케일링 구성도

![image](https://github.com/Hello-LSY/F-64/assets/81401604/e065e453-0cf9-4b94-b893-ddb98ab965fd)


## 기능

### 로그인/회원가입

![회원가입](https://github.com/user-attachments/assets/3c831da0-2500-4b05-89bc-c4e992d8a650)

![로그인 - Clipchamp로 제작](https://github.com/user-attachments/assets/8110faa5-ae05-416f-b94a-0190d27ad550)

### 자유게시판

![게시판생성,수정,제거 - Clipchamp로 제작](https://github.com/user-attachments/assets/14416179-31a1-425a-be3e-ef2a2874687a)

![댓글, 삭제, 추천 - Clipchamp로 제작](https://github.com/user-attachments/assets/8da03107-9ae8-46f1-86fd-3fdbc3db9eb0)

- CRUD 가능
- 댓글 기능
- 사진 첨부
- 추천 기능(동일 게시글 추천 중복안됨)

### 문의 게시판

![문의 - Clipchamp로 제작](https://github.com/user-attachments/assets/eefb5231-6f46-4b03-8d5e-15f05f62af3f)

- CRD 가능
- 익명, 비밀글 토글
- 익명 : admin 권한과 본인 계정을 제외하고 이름은 익명처리
- 비밀글 : admin 권한과 본인 계정을 제외하고 게시글 접근 불가
- 답변 기능/ 문의 상태

### 출사 신청 게시판

![출사지 - Clipchamp로 제작](https://github.com/user-attachments/assets/3a03938d-1514-4851-81ec-8faeaf28ff17)

- kakaoMap API 사용
- CRD 가능
- 동선 표시, 지도미리보기 기능

### 행사 일정

![행사일정 - Clipchamp로 제작](https://github.com/user-attachments/assets/fed53055-60ad-47f5-8160-1dc803f14dcf)

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



