# 서울 도로명주소 형상정보 수집 모듈
- - -
## 개발환경
개발언어: Java 11

빌드 툴: Maven

사용 API: VWorld(https://www.vworld.kr/dev/v4dv_2ddataguide2_s002.do?svcIde=sprd)

데이터베이스: postgreSQL + postGIS

- - -
## 사용방법
- 프로퍼티 파일을 변경하여 환경설경
1. src/main/resources/application.properties
    - API 키
    
2. src/main/resources/db.properties
    - Database 접속정보