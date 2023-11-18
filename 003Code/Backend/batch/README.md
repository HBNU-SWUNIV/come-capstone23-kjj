## 폴더 구조
ㄴ src/main/java/com/batch/batch

    ㄴ batch/order : 
        ㄴ aspect : 커스텀 Aspect
        ㄴ controller : 컨트롤러
        ㄴ handler : 커넥션 핸들러
        ㄴ job : Job 클래스
        ㄴ service : batch를 실행하기 위한 역할별 서비스
        ㄴ step : Step 클래스
        ㄴ task : tasklet 클래스
            ㄴ method : tasklet에서 호출하는 역할별 메서드
        
    ㄴ config : 설정 클래스
    
    ㄴ entity : DB와의 상호작용에 사용되는 오브젝트
    
    ㄴ tools : 유틸 클래스

## Batch 프로세스
- 정산 Job
<img src="https://github.com/HBNU-SWUNIV/come-capstone23-kjj/assets/94634916/f7af7d88-b388-4026-b149-e643450df72b" />

- 식재료 데이터 생성 Job
<img src="https://github.com/HBNU-SWUNIV/come-capstone23-kjj/assets/94634916/9a543b68-8438-4550-8605-2df6f5988645" />
