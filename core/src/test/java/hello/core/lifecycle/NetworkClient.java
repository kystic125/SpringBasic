package hello.core.lifecycle;

//import org.springframework.beans.factory.DisposableBean;
//import org.springframework.beans.factory.InitializingBean;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

//public class NetworkClient implements InitializingBean, DisposableBean {
public class NetworkClient {

    private String url;

    public NetworkClient() {
        System.out.println("생성자 호출, url = " + url);
    }

    public void setUrl(String url) {
        this.url = url;
    }

    // 서비스 시작시 호출
    public void connect() {
        System.out.println("connect: " + url);
    }

    public void call(String message) {
        System.out.println("call: " + url + " message: " + message);
    }

    // 서비스 종료시 호출
    public void disconnect() {
        System.out.println("close " + url);
    }
    // 1.
    // 인터페이스 활용 - 단점(스프링에 의존)
    // 의존관계 주입이 끝나면 호출
/*
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("NetworkClient.afterPropertiesSet");
        connect();
        call("초기화 연결 메세지");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("NetworkClient.destroy");
        disconnect();
    }
 */

    // 2.
    // 메서드 이름을 자유롭게 지정 가능
    // 스프링 빈이 스프링 코드에 의존하지 않는다
    // 코드가 아니라 설정 정보를 사용하기 때문에 코드를 고칠 수 없는 외부라이브러리에도 초기화, 죵료 메서드 적용 가능
/*
    public void init() throws Exception {
        System.out.println("NetworkClient.init");
        connect();
        call("초기화 연결 메세지");
    }

    public void close() throws Exception {
        System.out.println("NetworkClient.close");
        disconnect();
    }
 */

    // 3. 애노테이션 @PostConstruct, @PreDestroy 사용
    // 최신 스프링에서 가장 권장하는 방법 (애노테이션 하나만 붙이면 되므로 편리함)
    // 스프링이 아닌 다른 컨테이너에서도 동작
    // 단점: 외부 라이브러리에 적용하지 못함 -> 2번째 방법 (initMethod)사용하자
    @PostConstruct
    public void init() throws Exception {
        System.out.println("NetworkClient.init");
        connect();
        call("초기화 연결 메세지");
    }

    @PreDestroy
    public void close() throws Exception {
        System.out.println("NetworkClient.close");
        disconnect();
    }
}
