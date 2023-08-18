# ZanbanZero

2023 한밭대학교 컴퓨터공학과 캡스톤디자인 KJJ팀 백엔드  
(미완)

## Tools

<img src="https://img.shields.io/badge/Java-FF9900?style=for-the-badge&logo=OpenJDK&logoColor=white"/> <img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-badge&logo=SpringBoot&logoColor=white"/> <img src="https://img.shields.io/badge/Spring Batch-6DB33F?style=for-the-badge&logo=Batch&logoColor=white"/> <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=MySQL&logoColor=white"/>

## [ERD](https://www.erdcloud.com/d/qpTvHkbgBbqLyYYzZ)

<img src="https://github.com/HyeongMokJeong/Coding-Test/assets/94634916/fec9d298-749b-4d7a-a219-9d1334d5328a" />

## [API](http://kjj.kjj.r-e.kr:8080/apis)

- Path : /apis
  <img src="https://github.com/HyeongMokJeong/Coding-Test/assets/94634916/3d7f649c-90c2-41a5-b5e1-a60c11ff3c42" />

## [캐시를 활용한 조회 API 응답시간 이슈 해결](https://jeong-mok.tistory.com/8)

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

## [인덱싱을 이용한 API 응답 지연 이슈 해결](https://jeong-mok.tistory.com/13)

특정 API 호출 시 반영이 늦게 되거나 무시된다는 연락을 받았습니다.  
일부 기능 마비라는 점에 있어서 해당 이슈는 무겁다고 판단하였고, 원인 확인 작업에 들어갔습니다.

먼저 핵심 비즈니스 로직이 담겨있는 Service 단에서의 코드를 확인하였습니다.
<img src="https://github.com/HyeongMokJeong/Coding-Test/assets/94634916/305b01eb-e5e8-4724-91ff-254d8331c991" />

복잡한 로직은 아니었기에, 다른 알고리즘의 문제는 아니라고 보였습니다.  
유일한 조회 메서드인 findByDate()와 관련되어 있을 수도 있다고 판단되어 StoreState 클래스를 확인해보았습니다.

<img src="https://github.com/HyeongMokJeong/Coding-Test/assets/94634916/31a2e154-6722-4050-8c2e-18e8955528df" />  
<img src="https://github.com/HyeongMokJeong/Coding-Test/assets/94634916/c180d2da-757e-4348-8668-6116b8ace7c5" />

StoreState에서는 날짜와 관련된 date 필드를 String으로 사용하고 있었습니다.  
String타입의 date 필드를 풀 테이블 스캔하는 것이 문제일 수 있다고 판단하였고, 검증을 위해 인덱스를 추가했습니다.

<img src="https://github.com/HyeongMokJeong/Coding-Test/assets/94634916/08063af9-c4a4-4a96-9100-74c7eb297eec" />

확인을 부탁한 결과 해결되었다는 답변을 받았습니다.

<img src="https://github.com/HyeongMokJeong/Coding-Test/assets/94634916/da9f8431-86a9-4e00-8312-28c720f12334" />

문제를 해결한 후, String 타입을 LocalDate 타입으로 수정하였습니다.  
시간과 관련된 열이기 때문에 String 타입보다는 LocalDate 타입이 적합하다고 판단했습니다.

## [Batch를 이용한 자동 정산, 데이터 생성 시스템 구축](https://jeong-mok.tistory.com/10)

본 프로젝트에는 정산 시스템이 존재합니다.  
이용자가 직접 트리거를 통해 정산하는 방식과, 자동으로 실행되는 방식으로 구분할 수 있겠으나 편의성을 위해 후자를 택했습니다.  
대용량 데이터를 한번에 일괄 처리한다는 점에서 배치가 가장 적합한 솔루션이라고 판단했습니다.

또한 API 로직과는 관심사가 다르다고 판단하여 프로젝트를 분리했습니다.

<img src="https://github.com/HyeongMokJeong/Coding-Test/assets/94634916/1587e816-e431-4ec2-9b8a-019dbca99109" />
<img src="https://github.com/HyeongMokJeong/Coding-Test/assets/94634916/f0413689-adf0-49a1-8526-e406a243b14d" />

job, step, task로 구분하였고 Spring에서 제공하는 Scheduler를 이용했습니다.

<img src="https://github.com/HyeongMokJeong/Coding-Test/assets/94634916/c0ab00d7-91e9-4cc1-acd6-8748868c62fa" />

전체적인 배치 프로세스는 위와 같습니다.
