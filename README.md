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
