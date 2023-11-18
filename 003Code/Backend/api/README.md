## 자바 시스템 속성(VM 옵션)
- -Djava.net.preferIPv4Stack=true
- -Dpassword={Jasypt에서 사용할 Secret Key}

## 폴더 구조
ㄴ src/main/java/com/hanbat/zanbanzero  

    ㄴ aop : 커스텀 Aspect  
        ㄴ annotation : 커스텀 Annotaion  
        
    ㄴ auth : SpringSecurity를 이용한 인증 관련
        ㄴ jwt :  JWT 검증, 재발급
        ㄴ login : 로그인(일반, SSO)
        ㄴ monitor : 모니터링(Prometheus) 경로에 대한 접근 제한
        
    ㄴ config : 설정 클래스
    
    ㄴ controller : 컨트롤러 로직
    
    ㄴ dto : 클라이언트와의 통신에 사용되는 오브젝트
    
    ㄴ entity : DB와의 상호작용에 사용되는 오브젝트
    
    ㄴ exception : 예외 및 처리 관련
        ㄴ exceptions : 커스텀 Exception
        ㄴ handler : 예외 핸들링
        ㄴ tool : 예외 모니터링을 위한 유틸 클래스

    ㄴexternal : 외부 설정 파일 객체 매핑

    ㄴ repository : DAO. 레포지토리

    ㄴ service : 서비스 로직

## [API](http://kjj.kjj.r-e.kr:8080/apis)

- Path : /apis
  <img src="https://github.com/HyeongMokJeong/Coding-Test/assets/94634916/3d7f649c-90c2-41a5-b5e1-a60c11ff3c42" />
