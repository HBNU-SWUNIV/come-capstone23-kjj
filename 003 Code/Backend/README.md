# ZanbanZero

2023 한밭대학교 컴퓨터공학과 캡스톤디자인 KJJ팀 백엔드  
(미완)

## Tools
<img src="https://img.shields.io/badge/Java-FF9900?style=for-the-badge&logo=OpenJDK&logoColor=white"/> <img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-badge&logo=SpringBoot&logoColor=white"/> <img src="https://img.shields.io/badge/Spring Batch-6DB33F?style=for-the-badge&logo=Batch&logoColor=white"/> <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=MySQL&logoColor=white"/>

## [ERD](https://www.erdcloud.com/d/qpTvHkbgBbqLyYYzZ)
<img src="https://github.com/HyeongMokJeong/Coding-Test/assets/94634916/fec9d298-749b-4d7a-a219-9d1334d5328a" />

## [API](http://kjj.kjj.r-e.kr:8080/apis)
<img src="https://github.com/HyeongMokJeong/Coding-Test/assets/94634916/3d7f649c-90c2-41a5-b5e1-a60c11ff3c42" />

## [조회 API 응답시간 이슈 해결](https://jeong-mok.tistory.com/8)
특정 API 호출 시 약간의 지연의 발생한다는 피드백을 받아, 해당 API를 대상으로 성능 테스트를 진행해 보았습니다.  
200개의 커넥션을 생성하여 5초동안 유지시켰습니다.  
<img src="https://github.com/HyeongMokJeong/Coding-Test/assets/94634916/3024b971-a177-485b-81d7-b4089304a522" />  
테스트 결과, 응답시간은 평균 540.02ms, 최대 1.84s까지 소요되었으며 초당 약 350개의 요청을 처리하는 것을 확인하였습니다. 

속도 개선을 위한 방안을 고민해보았으나,  
DB Table에 불필요한 컬럼은 없었고, 단순한 전체 조회 기능이었기 때문에 쿼리에서의 개선 사항은 찾지 못했습니다.  
페이징을 적용하여 크기를 분할하는 방법도 고려했으나, 메뉴 구성은 자주 변경되지 않는다는 점과 해당 요청은 빈번하게 호출된다는 점에서 캐시를 적용하는 것이 더 적합하다고 판단했습니다.  

<img src="https://github.com/HyeongMokJeong/Coding-Test/assets/94634916/8895b964-0d52-474a-a9d7-cf8a713e8f01" />  

캐시 솔루션으로는 Redis를 채택했습니다.  
인메모리 DB로써 조회 속도가 빠르다는 점과 해당 요청에서 사용하는 List를 포함한 다양한 자료구조를 지원한다는 점이 그 근거였습니다.  

<img src="https://github.com/HyeongMokJeong/Coding-Test/assets/94634916/b9b2cf6d-4408-4636-8822-5e5cb2bac90e" />  

어노테이션을 통해 간편하게 적용해주었습니다.  

<img src="https://github.com/HyeongMokJeong/Coding-Test/assets/94634916/0e42d57a-4f49-452f-b4db-7b0edc303e81" />  

캐시 적용 이후 다시 성능 테스트를 진행하였습니다.  
평균 응답속도는 185ms, 최대 390ms로 각각 300%, 470% 정도의 속도개선이 발생했습니다.  
1초당 처리 가능한 요청 수 또한 1044.66개로 약 300% 증가하였음을 확인하였습니다.
