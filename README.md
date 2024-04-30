# Bigs Back-End 개발자 과제 - 김승우

## 개발환경

- JAVA 17
- Spring Boot 3.2.5
- Spring JPA
- MySQL
- 라이브러리 : Lombok, MapStruct 1.5.3


## 요청사항

### 과제
1. 경기도 의정부시 문충로74 지역의 단기예보를 확인할 수 있는 API 개발
2. 단기 예보 데이터는 공공데이터 포털의 ```기상청_단기예보 ((구)_동네예보) 조회서비스``` 를 사용하세요.

### 요청 기술 스택
- Kotlin/JAVA (완료)
- SpringBoot (완료)
- MySQL (완료)
- JPA (완료)

### API 과제

- 단기 예보를 DB에 저장하게 하는 API
    - POST 요청시 공공데이터 포털의 API를 호출하여 바로 DB 에 적재합니다.
- 단기 예보를 조회 하는 API
    - GET 요청시, DB 에 저장된 데이터를 조회합니다.
    - 데이터가 없을 경우, Http status 204 오류를 응답해야 합니다.

### 주의사항
- RestTemplate 을 사용해야하며, UrlConnection 을 직접 사용하지 않아야한다.
- 코드는 가독성이 좋아야한다.
- 멀티모듈로 구성하고, 모듈은 최대한 작고, 응집도가 높아야한다.



## 개발과정

### 멀티모듈 환경 설정
    root
    ├─ api-application  : Controller 모듈
    ├─ api-common       : 각 모듈에서 사용할 수 있는 모듈
    ├─ api-external     : 외부 API와 통신하는 모듈
    ├─ api-internal     : 내부 DB와 통신하는 모듈
    ├─ api-server       : Server 모듈
    │
    ├─ build.gradle
    └─ settings.gradle

<img src="https://github.com/KIMSEUNGWO/Room_Project/assets/128001994/1d27a7fd-fcc4-42fb-9599-0dfca00162da" style="height:300px;" alt="도식"/>

### 예외처리

RestControllerAdvice : ```ExceptionHandlerController```

```DataException``` : 외부API 예외처리, 요청데이터 및 과 함께 ```HTTP_STATUS : 400 BAD_REQUEST``` 를 반환한다.

```NotFoundDataException``` : 외부API 조회결과 데이터가 존재하지 않은경우, DB에 해당 날짜의 데이터가 존재하지 않은경우 ```HTTP_STATUS : 204 NO_CONTENT``` 를 반환한다.

단기예보API 내부 예외코드는 ```ErrorCode``` enum 에 정의되어있다.

### 응답객체

모든 데이터의 Response 는 ```ResponseMessage``` 객체를 사용한다.

```
    {
        "result" : "OK",
        "message" : "조회성공"
    }
```

<br>

데이터를 반환하는경우 ```ResponseMessage```를 상속받은 ```ResponseData<?>``` 객체를 사용한다

```
    {
        "result" : "OK",
        "message" : "조회성공",
        "data" : [...]
    }
```


### 과제 1.

- POST 요청 시 공공테이터 포탈 API에서 데이터를 조회하여 DB에 저장해야한다.

**POST** /demo/v1/post

Content-Type: application/json
```
    {
        "date" : "20241231"
    }
```

<br>

<details>
    <summary>과제1 설명</summary>

요청데이터와 검증은 ```RequestDto``` class 가 담당합니다.
사용자로부터 date 값을 요청받는다. 요청데이터가 null인경우 현재일 기준 이전 날이 입력됩니다.


```
@Component
public class CustomTemplate {

    private final RestTemplate restTemplate;
    private final HttpEntity<?> entity;

    public CustomTemplate() {
        this.restTemplate = new RestTemplate();
        this.entity = new HttpEntity<>(getHeaders());
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Accept", "*/*;q=0.9");
        return headers;
    }

    public <T> T get(URI uri, Class<T> clazz) {
        return restTemplate.exchange(uri, HttpMethod.GET, entity, clazz).getBody();
    }
    
}
```

RestTemplate 을 ```CustomTemplate``` class 로 감싸고 Bean으로 등록시켜 싱글톤으로 구성했습니다.

요청시 uri 주소 ```customTemplate.get()``` 요청 시 uri 주소를 통해 GET 요청이 되며 clazz 타입으로 반환됩니다.

제네릭을 사용한 이유는 외부에서 타입을 주입 받음으로써 객체 간의 결합도를 낮추고,
요청 uri, 반환타입이 변경에 수정없이 대응할 수 있기 때문에 사용했습니다.


조회 결과는 ```JsonData```class에 담겨 Controller로 반환됩니다.

단기예보API 내부 예외코드는 ```ErrorCode``` enum 에 정의 되어있으며, 조회결과의 errorCode를 확인하고 그에 맞는
코드와 함께 DataException이 발생합니다.

</details>