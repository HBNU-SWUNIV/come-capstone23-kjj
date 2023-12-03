# Source Code

## 실행 가이드
- kjj-quick-start  
  https://www.dropbox.com/scl/fi/qmhc8xl94xdpg8miwvh00/kjj-quick-start.zip?rlkey=fpw63x24skscm6h2yet11ny0h&dl=0
  
- 구성 : 웹 서버, API 서버, Batch 서버, DB서버 * 2  

- 구조  
    kjj-quick-start  
	      ㄴ db : 데이터베이스 실행을 위한 Docker-compose.yml 및 초기화 파일  
	      ㄴ backend.tar : API 서버 이미지 tar 파일  
	      ㄴ batch.tar : Batch 서버 이미지 tar 파일  
	      ㄴ frontend.tar : 관리자 웹, 소비자 앱을 라우팅하는 Nginx 웹 서버 이미지 tar 파일  
	      ㄴ run.sh : 실행 환경을 구축하는 쉘 스크립트

- 조건
  - 쉘 스크립트 실행 가능 환경
  - Docker 및 Docker-compose 실행 가능 환경  

- 사용 포트
  - 80, 81 : Nginx(관리자 웹, 소비자 웹)  
  - 8080 : API 서버  
  - 8888 : Batch 서버  
  - 3306 : 메인 DB 서버  
  - 3307 : Batch DB 서버

 - 실행 방법
0. kjj-quick-start 설치 및 압축 해제

1. 내부 root 경로로 이동 (run.sh 파일이 있는 kjj-quick-start 내부)
<img src="https://github.com/HBNU-SWUNIV/come-capstone23-kjj/assets/94634916/5cc63348-7474-4a53-acc8-537d916dc62c"/>

3. 쉘 스크립트 실행 ($ sh run.sh)
<img src="https://github.com/HBNU-SWUNIV/come-capstone23-kjj/assets/94634916/44cfd593-8c0d-4bee-94fa-5f6f4e885c16" />

## 주의사항
 - 다른 Open Source SW를 사용하는 경우 저작권을 명시해야 함
 - 산학연계 캡스톤의 경우 기업의 기밀이 담긴 데이터는 제외할 것.
 <span style="color:red">
 - **기업 기밀 데이터가 Github에 공개되었을 시의 책임은 공개한 학생에게 있음**
 </span>
