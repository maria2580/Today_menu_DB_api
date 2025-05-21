# 오늘 뭐 먹지? - 백엔드 API (Today's Menu DB API)

## 🌟 소개

**Today's Menu DB API**는 "오늘 뭐 먹지?" 안드로이드 애플리케이션을 위한 백엔드 서버입니다. 이 API는 식단 댓글, 식단 이미지 (점심/저녁), 그리고 메뉴에 대한 반응(좋아요/싫어요) 데이터를 관리하고 제공하는 역할을 합니다.

Spring Boot, Spring Data JPA를 기반으로 구축되었으며, RESTful API 형태로 안드로이드 클라이언트와 통신합니다.

## ✨ 주요 기능

* **댓글 관리:**
    * 특정 날짜의 댓글 조회
    * 새로운 댓글 추가 (작성 시간 자동 기록)
* **식단 이미지 관리:**
    * 점심/저녁 식단 이미지 업로드 (Base64 인코딩된 문자열 사용)
    * 점심/저녁 식단 이미지 조회 (Base64 인코딩된 문자열로 제공)
    * 이미지 파일은 서버 파일 시스템에 저장 (`D:\today_menu_api\images` 또는 `/home/ubuntu/today_menu/images`)
* **반응(좋아요/싫어요) 관리:**
    * 특정 날짜의 좋아요/싫어요 개수 조회
    * 좋아요/싫어요 개수 증가 및 감소

## 🛠️ 기술 스택

* **언어:** Java
* **프레임워크:** Spring Boot
    * Spring Web: RESTful API 개발
    * Spring Data JPA: 데이터베이스 연동
* **데이터베이스:** 관계형 데이터베이스 (예: H2, MySQL, PostgreSQL - 설정에 따라 다름)
* **라이브러리:**
    * Lombok: 보일러플레이트 코드 감소
    * Apache Commons IO: 파일 처리 유틸리티
* **빌드 도구:** Gradle 또는 Maven (Spring Initializr 기본 설정에 따름)

## 📄 API 엔드포인트

### Comments API

* **댓글 조회**
    * **Method:** `GET`
    * **Path:** `/comments/{day}`
    * **Path Variable:** `day` (String, 형식: "YYYY-MM-DD") - 조회할 날짜
    * **Response Body (JSON):**
        ```json
        {
          "content": ["댓글 내용1", "댓글 내용2", ...],
          "date_written": ["YYYY-MM-DD-HH-MM-SS", "YYYY-MM-DD-HH-MM-SS", ...]
        }
        ```
    * **Description:** 해당 날짜의 모든 댓글 내용과 작성 시간을 반환합니다.

* **댓글 추가**
    * **Method:** `POST`
    * **Path:** `/add/comments/{date}`
    * **Path Variable:** `date` (String, 형식: "YYYY-MM-DD") - 댓글을 추가할 메뉴의 날짜
    * **Request Body (String - text/plain):**
        ```
        댓글 내용입니다.
        ```
    * **Response Body (JSON):**
        ```json
        {
          "content": "댓글 내용입니다."
        }
        ```
    * **Description:** 새로운 댓글을 등록합니다. 서버에서 현재 시간을 `dateWritten`으로 자동 저장합니다.

### Images API

* **점심 이미지 조회**
    * **Method:** `GET`
    * **Path:** `/images/lunch/{day}`
    * **Path Variable:** `day` (String, 형식: "YYYY-MM-DD") - 조회할 날짜
    * **Response Body (String - text/plain):** Base64 인코딩된 이미지 문자열
    * **Description:** 해당 날짜의 점심 이미지를 Base64 문자열로 반환합니다. 이미지가 없으면 `no_image.png`가 반환됩니다.

* **저녁 이미지 조회**
    * **Method:** `GET`
    * **Path:** `/images/dinner/{day}`
    * **Path Variable:** `day` (String, 형식: "YYYY-MM-DD") - 조회할 날짜
    * **Response Body (String - text/plain):** Base64 인코딩된 이미지 문자열
    * **Description:** 해당 날짜의 저녁 이미지를 Base64 문자열로 반환합니다. 이미지가 없으면 `no_image.png`가 반환됩니다.

* **점심 이미지 추가/수정**
    * **Method:** `POST`
    * **Path:** `/add/images/lunch/{day}`
    * **Path Variable:** `day` (String, 형식: "YYYY-MM-DD") - 이미지를 추가/수정할 날짜
    * **Request Body (String - text/plain):** Base64 인코딩된 이미지 문자열
    * **Response:** `200 OK` (성공 시)
    * **Description:** 해당 날짜의 점심 이미지를 업로드(또는 덮어쓰기)합니다.

* **저녁 이미지 추가/수정**
    * **Method:** `POST`
    * **Path:** `/add/images/dinner/{day}`
    * **Path Variable:** `day` (String, 형식: "YYYY-MM-DD") - 이미지를 추가/수정할 날짜
    * **Request Body (String - text/plain):** Base64 인코딩된 이미지 문자열
    * **Response:** `200 OK` (성공 시)
    * **Description:** 해당 날짜의 저녁 이미지를 업로드(또는 덮어쓰기)합니다.

### Reactions API

* **좋아요 개수 조회**
    * **Method:** `GET`
    * **Path:** `/likes/{day}`
    * **Path Variable:** `day` (String, 형식: "YYYY-MM-DD") - 조회할 날짜
    * **Response Body (Integer - text/plain):** 해당 날짜의 좋아요 개수
    * **Description:** 특정 날짜의 총 좋아요 수를 반환합니다.

* **싫어요 개수 조회**
    * **Method:** `GET`
    * **Path:** `/dislikes/{day}`
    * **Path Variable:** `day` (String, 형식: "YYYY-MM-DD") - 조회할 날짜
    * **Response Body (Integer - text/plain):** 해당 날짜의 싫어요 개수
    * **Description:** 특정 날짜의 총 싫어요 수를 반환합니다.

* **좋아요 증가**
    * **Method:** `POST`
    * **Path:** `/add/likes/{day}/plus`
    * **Path Variable:** `day` (String, 형식: "YYYY-MM-DD") - 좋아요를 증가시킬 날짜
    * **Response:** `200 OK` (성공 시)
    * **Description:** 해당 날짜의 좋아요 개수를 1 증가시킵니다.

* **좋아요 감소**
    * **Method:** `POST`
    * **Path:** `/add/likes/{day}/minus`
    * **Path Variable:** `day` (String, 형식: "YYYY-MM-DD") - 좋아요를 감소시킬 날짜
    * **Response:** `200 OK` (성공 시)
    * **Description:** 해당 날짜의 좋아요 개수를 1 감소시킵니다.

* **싫어요 증가**
    * **Method:** `POST`
    * **Path:** `/add/dislikes/{day}/plus`
    * **Path Variable:** `day` (String, 형식: "YYYY-MM-DD") - 싫어요를 증가시킬 날짜
    * **Response:** `200 OK` (성공 시)
    * **Description:** 해당 날짜의 싫어요 개수를 1 증가시킵니다.

* **싫어요 감소**
    * **Method:** `POST`
    * **Path:** `/add/dislikes/{day}/minus`
    * **Path Variable:** `day` (String, 형식: "YYYY-MM-DD") - 싫어요를 감소시킬 날짜
    * **Response:** `200 OK` (성공 시)
    * **Description:** 해당 날짜의 싫어요 개수를 1 감소시킵니다.

## 📁 프로젝트 구조

주요 패키지 구성은 다음과 같습니다:

* **`com.primitive.todayMenuDBApi`**: 루트 패키지
    * **`controller`**: API 요청을 처리하는 컨트롤러 클래스 위치
        * `CommentController.java`
        * `ImageController.java`
        * `ReactionController.java`
    * **`entity`**: JPA 엔티티 클래스 위치 (데이터베이스 테이블과 매핑)
        * `Comment.java`
        * `DislikeCount.java`
        * `ImageEntity.java`
        * `LikeCount.java`
    * **`repository`**: Spring Data JPA 리포지토리 인터페이스 위치
        * `CommentRepository.java`
        * `DislikeCountRepository.java`
        * `ImageRepository.java`
        * `LikeCountRepository.java`
    * **`service`**: 비즈니스 로직을 처리하는 서비스 클래스 및 인터페이스 위치
        * `CommentService.java` (Interface)
        * `CommentServiceImpl.java`
        * `ImageService.java` (Interface)
        * `ImageServiceImpl.java`
        * `ReactionService.java` (Interface)
        * `ReactionServiceImpl.java`
    * `TodayMenuDbApi.java`: Spring Boot 메인 애플리케이션 클래스

## ⚙️ 설정 및 실행

### 사전 준비 사항

* JDK 8 이상 (Spring Boot 버전에 따라 권장 JDK 상이)
* Gradle 또는 Maven
* 데이터베이스 서버 (MySQL, PostgreSQL 등) 또는 H2 내장 DB 사용 가능

### 설정

1.  **데이터베이스 설정**:
    * `src/main/resources/application.properties` (또는 `application.yml`) 파일에 데이터베이스 연결 정보를 설정합니다.
    ```properties
    # 예시 (MySQL)
    spring.datasource.url=jdbc:mysql://localhost:3306/your_database_name?useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8
    spring.datasource.username=your_db_user
    spring.datasource.password=your_db_password
    spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

    spring.jpa.hibernate.ddl-auto=update # 또는 validate, none 등 개발 환경에 맞게 설정
    spring.jpa.show-sql=true
    spring.jpa.properties.hibernate.format_sql=true
    ```

2.  **이미지 저장 경로 확인 및 설정**:
    * `ImageServiceImpl.java` 내의 `uploadDir` 변수에 이미지 파일이 저장될 경로가 정의되어 있습니다.
        * Windows: `D:\today_menu_api\images`
        * Linux/macOS: `/home/ubuntu/today_menu/images`
    * 필요에 따라 이 경로를 수정하거나, 해당 경로에 디렉토리를 생성하고 적절한 쓰기 권한을 부여해야 합니다.
    * 기본적으로 `no_image.png` 파일이 `uploadDir` 경로에 존재해야 이미지가 없을 때 정상적으로 기본 이미지를 반환할 수 있습니다.

### 실행

1.  프로젝트를 빌드합니다.
    * Gradle: `./gradlew build`
    * Maven: `./mvnw package`
2.  빌드된 JAR 파일을 실행합니다.
    * `java -jar build/libs/todayMenuDBApi-0.0.1-SNAPSHOT.jar` (빌드 결과물 경로 및 이름 확인)

또는 Spring Boot IDE (IntelliJ IDEA, Eclipse STS 등)에서 직접 애플리케이션을 실행할 수 있습니다.

## 📝 추가 정보

* 본 API는 안드로이드 애플리케이션 "오늘 뭐 먹지?"에서 사용되는 것을 전제로 설계되었습니다.
* 이미지 처리는 Base64 인코딩/디코딩 방식을 사용하며, 파일 시스템에 이미지를 저장합니다.
* `ImageServiceImpl`의 `osName` 및 `sep` 변수를 통해 OS에 따른 파일 경로 구분자를 자동으로 처리합니다.
* `ReactionController`의 좋아요/싫어요 증감 API는 현재 단순 증감만 처리하며, 사용자별 중복 투표 방지 등의 로직은 포함되어 있지 않습니다. 필요시 해당 로직을 추가 구현해야 합니다.
* 안드로이드 앱의 "건의사항" 관련 API(`POST /add/Suggestion/{date}`)는 현재 백엔드 코드에 구현되어 있지 않습니다.

---
이 README가 프로젝트의 백엔드 API를 이해하고 사용하는 데 도움이 되기를 바랍니다.
