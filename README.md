# GGtics Recruitement LoL Score Search Service Implematition

1. 기술 스택
    - Kotlin
      - Java로 구현 후 Kotlin으로 마이그레이션
    - Spring Boot
2. 과제
    1. 소환사 검색 후 최근 20경기의 전적 리스트 및 통계 API 개발
    2. 필수 스펙 - 반드시 구현 필요
        1. 반드시 Riot API 사용 (전적검색 서비스 크롤링 금지)
        2. URL은 “/api/summoner/{소환사명}”와 같이 구성
        3. 위 API 호출 시 프론트에서 화면 구성한다 가정하고 아래 정보 반환
            1. 20경기의 전적 정보
            2. 소환사의 통계 반환
    3. 가산점
        1. `테스트` 케이스 작성
        2. `객체지향에 대한 고민`이 녹아있는 코드
        3. `협업` 관점에서 `가독성 좋은 코드` 및 `커밋 메세지`
        4. `인분`과 같은 종합 평가 지표 추가
           1. 인분에 대한 자료 참고 혹은 스스로의 정의를 내릴 필요
        5. `Riot API의 Rate Limit`를 고려해 `효율적`으로 API를 사용할 수 있는 모듈 구현

3. 프로젝트 구조
   - 프로덕션
     - MVC 패턴의 코틀린으로 작성
     - Service 계층 생략
       - 이유: 원자성을 보장하는 작업이 존재하지 않음
     - `LoLScoreStatsCalculator`: 전적을 계산하는 실제 도메인 클래스
     - `PreProcessedData`: 전적을 가공하기 위한 DTO 클래스, 도메인 로직에 사용됨
     - `WebClientFacade`: WebClient를 Facade로 감싼 Utils성 클래스
   - 테스트
     - `match-dtos.json`: 테스트를 위한 데이터 파일
     - `JsonFileConverter`: 테스트 데이터 제공을 위한 파일 to DTO 매퍼
     - `GlobalTestUtils`: 테스트를 중복없이, 편리하게 작성하기 위한 Utils 클래스
     - `AcceptanceTest`: 추후 인수테스트 클래스 추가가 될 경우 SpringBootTest를 Caching 가능하게 함 (일관된 환경 제공)
