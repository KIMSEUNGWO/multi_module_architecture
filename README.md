# Bigs Back-End 개발자 과제 - 김승우

## API 문서

|                 HTTP METHOD                  |   HTTP URI    |     HEADER      |      BODY       |
|:--------------------------------------------:|:-------------:|:---------------:|:---------------:|
| <strong style="color:steelblue">GET</strong> | /demo/v1/get  | date : 20241231 |        -        |
|  <strong style="color:green">POST</strong>   | /demo/v1/post |        -        | date : 20241231 |

<br>

## 개발환경

- JAVA 17
- Spring Boot 3.2.5
- Spring JPA
- MySQL
- 라이브러리 : Lombok, MapStruct 1.5.3

<br><br><br>

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

<br><br><br>

## 개발과정

<br>

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

<p align="center">
    <img src="https://github.com/KIMSEUNGWO/Room_Project/assets/128001994/1d27a7fd-fcc4-42fb-9599-0dfca00162da" style="height:300px;" alt="도식"/>
</p>

<br><br>

### ERD 설계
<p align="center">
  <img src="https://github.com/KIMSEUNGWO/multi_module_architecture/assets/128001994/5b26659b-ea7b-4287-9fd8-4fbdc851913e"alt="ERD"/>
</p>

<br><br>

### 예외처리

RestControllerAdvice : ```ExceptionHandlerController```

```DataException``` : 외부API 예외처리, 요청데이터 및 과 함께 ```HTTP_STATUS : 400 BAD_REQUEST``` 를 반환한다.

```NotFoundDataException``` : 외부API 조회결과 데이터가 존재하지 않은경우, DB에 해당 날짜의 데이터가 존재하지 않은경우 ```HTTP_STATUS : 204 NO_CONTENT``` 를 반환한다.

```ErrorCode enum``` : 단기예보API 내부 예외코드

<br><br>

### Request

데이터의 Request 는 ```RequestDto``` 객체가 담당한다.

요청데이터가 null인경우 default 값인 현재일 기준 이전 날을 데이터로 사용한다.

요청데이터가 형식과 일치하지 않거나, 현재일 기준 3일전 또는 이후라면 ```DataException``` 이 발생한다.

```
    {
        "date" : "20241231" // yyyyMMdd String
    }
```

<br><br>

### Response

데이터의 Response 는 ```ResponseMessage``` 객체를 사용한다.

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

<br><br>

### 과제 1.

- POST 요청 시 공공테이터 포탈 API에서 데이터를 조회하여 DB에 저장해야한다.

#### POST /demo/v1/post

#### Content-Type: application/json
```
    {
        "date" : "20241231"
    }
```

<details>
    <summary><strong>과제1 설명</strong></summary>
<br>

Application - External - Application - Internal 구조에서 각 External과 Internal 사이에 객체공유가 안되는것을 확인했습니다.
따라서 사이에 DataBinder 객체를 사용해 객체를 변환시키며 모듈간의 객체사용을 원할하게 만들었습니다.


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

이 과정에서 단기예보API 내부예외가 발생한 경우 ```DataException```, 요청한 데이터가 없다면 ```NotFountDataException``` 이 발생합니다.

```api-external``` 모듈의 객체를 바인딩 하고 ```api-internal``` 모듈로 이동합니다.

JPA로 데이터를 저장합니다.

정상흐름 예시

```
{
    "result": "OK",
    "message": "정상적으로 저장되었습니다."
}
```

</details>

<br><br>

### 과제 2.

- GET 요청 시 DB에 저장된 데이터를 조회한다. 데이터가 없을 경우 ```HTTP STATUS 204``` 를 반환한다.

#### GET /demo/v1/get?date=20241231

<details>
    <summary><strong>과제2 설명</strong></summary>
<br>

1. 사용자로부터 요청받은 날짜를 DB에서 조회합니다.

2. 데이터가 존재하지 않으면, ```NotFountDataException``` 이 발생하고 ExceptionHandler에 의해 HTTP STATUS 204를 반환합니다.

데이터 조회 완료 예시

```
{
    "result": "OK",
    "message": "조회성공",
    "data": [
        {
            "date": "2024-04-29 06:00",
            "detailData": [
                "1시간 기온 : 15℃",
                "풍속(동서성분) : -1.8m/s",
                "풍속(남북성분) : -0.9m/s",
                "풍향 : 65deg",
                "풍속 : 2.1m/s",
                "하늘상태 : 흐림",
                "강수형태 : 없음",
                "강수확률 : 30%",
                "파고 : 0m",
                "1시간 강수량 : 강수없음",
                "습도 : 75%",
                "1시간 신적설 : 적설없음"
            ]
        },
        {
          ...
        }
    ]
}
```
</details>


<br><br><br>

## 과제를 마치며

### MapStruct

모듈간의 객체변환을 일일이 하다가 MapStruct 라는 라이브러리를 알게되었고, 실제로 적용해보았습니다.
1. ```External.dto.JsonMapper```
2. ```Internal.dto.ResultDataMapper```

다른 매핑라이브러리보다 속도가 빠르다고하는데 이번 과제에서는 간단한 매핑만 하였기에 체감은 되지 않았지만, 다른 부분에서는 만족했던 라이브러리였습니다.

아래는 제가 MapStruct를 사용하면서 느낀점입니다.
- 컴파일시점에 코드가 생성된다.
  - 구현체가 생성될 때 코드를 직접 확인하고 문제가 생기는 부분을 바로 알 수 있어서 좋았습니다.
- 변환 코드와 서비스 로직을 분리할 수 있다.
  - 이 부분이 가장 마음에 들었습니다. 서비스 로직에서 객체변환은 코드를 지저분하게 만드는 경향이 있는데 MapStruct 를 적용하면서 코드가 대폭 줄어들었습니다.
- MapStruct에서 자체적으로 Lombok을 사용하기 때문에 Getter, Setter가 불필요하게 증가한다.
  - 변환과정에서 값을 넣는 행위가 필수적으로 일어나지만 이 부분을 getter와 setter가 사용된다는점에서 아쉬웠습니다.
  - 객체지향언어 특성인 캡슐화 또는 은닉화같이 최대한 숨기고 하나의 단위로 사용되게 하기위해 Builder Pattern 등 다양한 디자인패턴이 개발되어왔는데 객체변환을 위해 이 부분을 포기해야하는게 쉽지않았습니다.

MaoStruct 기술은 여러방면에서 편리했지만, 단순히 모듈 간의 객체변환 용도로만 사용해야할 것 같고, 
MapStruct가 적용된 객체를 하나의 인터페이스로 묶어 통제하는 것도 좋을 것 같다는 생각을 했습니다.

<br><br>

### 멀티모듈설계

멀티모듈에 대한 지식이 전무한 상태로 이번 과제를 받았고, 보내주신 우아한기술블로그를 링크를 참고하며 설계해보았습니다.
인터넷에 나와있는 장/단점 말고, 이 기술을 처음 접한 사람의 느낀점으로는

1. 독립적으로 개발을 할 수 있다.
   - 각 모듈마다 하나의 기능만 수행함으로써, 다른 기능과의 결합도가 낮아 객체지향적인 설계라고 느꼈습니다.
   - 다만 위와 같이 MapStruct 같은 매핑라이브러리를 추가로 사용해야되거나, 그 과정에서 의존성이나 환경설정에 이전보다 더 시간을 소비해야되는 조금의 아쉬움이 있었습니다.


2. 팀 단위의 개발이 이전보다 편해질 것 같다.
   - 각 인원이 하나의 모듈을 담당하고, Request, Response 객체만 잘 정의되어있다면 하나의 프로젝트에서 협업하는 것보다 더 효율적인 개발이 가능할 것같습니다.


3. 테스트 코드 작성이 불편했다.
   - 모듈에서의 테스트 코드작성이 처음이라 그럴 수 있겠지만, JpaRepository Bean 등록이나 환경설정에 더 많은 시간을 쏟았습니다. 이후 개발에서는 더 공부해서 이 부분은 보안할 수 있을것같습니다.

<br><br>

### 느낀점

처음 과제를 접하고 이 멀티모듈에 대한 근본적인 의문점이 있었습니다. '굳이?', '패키지 자체를 잘 나누어서 개발하면 모듈과 같은것 아닌가?' 라는 생각이 지배적이었습니다.
하지만 이후 의존성과 환경세팅 등 불편한점을 감수 하고서라도 사용하는 이유를 깨닫게되었습니다.
'모듈은 독립적으로 사용될 수 있다.' 라는 의미는 '하나의 서버로 사용할 수 있다.' 가 된다는것을 조금 늦게나마 이해할 수 있었습니다.
(모듈 - 패키지, 독립적 - 하나의 기능) 으로 묶어 잘못된 방법으로 개발하고 있었던 것입니다.

단기예보API 를 담당하는 모듈, 내부 DB와 연결하는 모듈 두개로 완벽하게 나누고 각 서버를 실행 해 통신하며, 
한쪽 서버의 예기치 못한 다운이 일어나면 Redis에 임시저장했다가 일정시간 이후에 데이터를 전송하는 등 다양한 방법으로 개발을 할 수 있었는데 이것을 처음에 알지 못했던 것이 너무 아쉬웠습니다.

새로운 것을 배우는 건 처음에는 이해도 안되고, 어렵지만 배우고 나면 그 이유를 알 수 있고 내 것으로 만들 수 있다는 게 좋은 것 같습니다.
짧은 1주일이었지만 많은 것을 배웠고, 재미있었습니다.

기회주셔서 감사합니다.