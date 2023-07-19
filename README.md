# SpringBoot-Project-MnM

* MBTI 데이트_매칭_서비스

---
# 🖥️ 프로젝트 소개
성향이 맞는 사람을 찾기 쉽도록 젋은 세대들 사이 유행하는 MBTI 기반 매칭 사이트
---

## 주제선정 계기
```
젋은 세대들 사이 유행하는 mbti기반 데이트 매칭 사이트를 만들게 되었습니다. MBTI는
사람들의 성격을 파악하는 데 효과적인 도구입니다.

이 애플리케이션을 통해 사용자는 자신의 연애 성격 유형을 더 잘 이해하고 , 이에 따라 성격 유형이
비슷하거나 상호 보완적인 이성을 찾을 수 있습니다.

또한 사용자가 자신과 다른 성격 유형을 가진 사람들과의 관계를 이해하고 개선하는 데 도움을 줄 수 있습니다.
```


🕰️ 개발 기간
* 23.06.19일 ~ 23.07.14일

🙆‍♂️ 멤버 구성
* 팀장 : 나길태 
* 팀원 : 김성찬 
* 팀원 : 박준수 
* 팀원 : 서요한 
* 팀원 : 최혁 

⚙️ 개발 환경

* `Java 17`
* IDE : `IntelliJ`
* Framework : `Springboot`
* FrontEnd : `tailwind`, `daisyUI`, `thymeleaf`
* DataBase : `myspl` , `redis` , 
* server : `CentOs`
* Cloud Services : `Naver Cloud Platform`
* API managing  `postman`
* Deploy : `docker` , `nginx`
* Etc : `Google natural api`, `pinpoint`


## 📌주요 기능
* 회원가입 및 로그인
* 커뮤니티 게시판(자유,MBTI)
* 채팅기능( 1:1 , 단체 )
* 마이페이지
* 호감 표시
* 추천
* 구글 자연어 처리 API


## API 문서
  > [API 문서](https://documenter.getpostman.com/view/20535647/2s946e8sT1)

## 🌍요구사항과 WBS
[요구사항과 WBS](https://docs.google.com/spreadsheets/d/1fHgLr55wHPIZOT785M69IuWmlH8rhuBgbvxphU7vwJw/edit#gid=0)

## 📌코드 컨벤션
[캠퍼스 핵데이 Java 코딩 컨벤션](https://naver.github.io/hackday-conventions-java/)

---
## 🚧 프로젝트 구조 
![image](https://github.com/LL-MnM/MnM/assets/114526859/7b06448e-f5ea-4c3a-b4ec-c8f774ddcb4f)

## 🤖ERD
<img width="955" alt="image" src="https://github.com/LL-MnM/MnM/assets/114526859/3c36ad4e-f224-4403-88f3-d42e40cde772">

## 🚧 사용 화면

<details>
<summary>메인</summary>
<div markdown="1"> 
  <img width="1201" alt="image" src="https://github.com/LL-MnM/MnM/assets/114526859/cd92b4b7-9224-427a-af2d-064308176e99">
</div>
</details>

<details>
<summary>MBTI 검사 페이지</summary>
<div markdown="2">
  <img width="1112" alt="image" src="https://github.com/LL-MnM/MnM/assets/114526859/e21fbf56-3704-4f19-8925-26d31fd7cf32">
</div>
</details>

<details>
<summary>추천 , 호감 페이지</summary>
<div markdown="3">   
  <img width="1141" alt="image" src="https://github.com/LL-MnM/MnM/assets/114526859/0aa57d3a-7c38-45a2-827d-35c34c30a729">
</div>
</details>

<details>
<summary>호감목록</summary>
<div markdown="4">
  <img width="1165" alt="image" src="https://github.com/LL-MnM/MnM/assets/114526859/d9e7f615-3e7c-40cb-b3ef-790a2c96e551">
</div>
</details>

<details>
<summary>게시판</summary>
<div markdown="5">
  <img width="1134" alt="image" src="https://github.com/LL-MnM/MnM/assets/114526859/fbbe8676-6c72-4339-87c1-28aa9ea4f84d">
</div>
</details>

<details>
<summary>채팅방 목록 (실시간, 1:1)</summary>
<div markdown="5">
<img width="1142" alt="image" src="https://github.com/LL-MnM/MnM/assets/114526859/8b90b473-bdf1-4a3b-8ae3-c77f8f39a21f">
</div>
</details>

<details>
<summary>채팅방 내부</summary>
<div markdown="5">
<img width="1105" alt="image" src="https://github.com/LL-MnM/MnM/assets/114526859/b25adfc6-054c-46a8-bc3e-fd356afe5eb7">
</div>
</details>

<details>
<summary>로그인/회원가입 , 마이페이지</summary>
<div markdown="5">
<img width="1162" alt="image" src="https://github.com/LL-MnM/MnM/assets/114526859/4955fe8d-e545-43a5-a0b1-80d3b7b2c050">
</div>
</details>

## 💻 사용 예시

### git clone

>`git clone https://github.com/LL-MnM/MnM.git`

### docker build

>`docker build -t mnm .`

### docker run

`docker run -d --name mnm -p 8080:8080  mnm`

### 주의 사항
1. `secret.default`를 자신에게 맞게 설정 한 후 사용
2. 현재 도커 파일의 경우 **prod profile** 사용중이며, `pinpoint`를 사용하도록 되어 있음 사용시 자신에게 맞게 고쳐서 사용해야 함
3. **prod**의 경우 `redis cluster`를 사용하도록 구현되어 있음, 이 또한 자신에게 맞도록 구현
4. **s3**의 경우 `naver object storage`, `aws s3`중 **naver**를 사용하고 있으며 자신이 사용하려는 플랫폼을 **profile** 설정에 설정하면 됨(예: **active: aws**)
