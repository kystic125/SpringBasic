package hello.core.singleton;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import static org.junit.jupiter.api.Assertions.*;

class StatefulServiceTest {

    @Test
    void statefulServiceSingleton() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);
        StatefulService statefulService1 = ac.getBean(StatefulService.class);
        StatefulService statefulService2 = ac.getBean(StatefulService.class);

        // ThreadA: 사용자 A가 10000원을 주문
        statefulService1.order("userA", 10000);
        // ThreadB: 사용자 B가 10000원을 주문
        statefulService2.order("userB", 20000);

        // ThreadA: 사용자 A가 주문 금액 조회
        // (의도는 1만원이 나왔으면 함)
        int price = statefulService1.getPrice();
        System.out.println("price = " + price); // 결과는 2만원

        Assertions.assertThat(statefulService1.getPrice()).isEqualTo(20000);
    }

    static class TestConfig {
        @Bean
        public StatefulService statefulService() {
            return new StatefulService();
        }
    }

}