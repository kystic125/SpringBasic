package hello.core;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import hello.core.member.MemoryMemberRepository;
import hello.core.order.OrderService;
import hello.core.order.OrderServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// AppConfig: 구체 클래스를 선택한다
@Configuration
public class AppConfig {

    @Bean
    public MemberService memberService() {
        System.out.println("call AppConfig.memberService");
        return new MemberServiceImpl(memberRepository());
        // 생성자 주입
    }

    // 나중에 메모리멤버리파지토리->DB로 바꾸게 되면 해당 부분만 바꾸면 됨
    @Bean
    public MemoryMemberRepository memberRepository() {
        System.out.println("call AppConfig.memberRepository");
        return new MemoryMemberRepository();
    }

    @Bean
    public OrderService orderService() {
        System.out.println("call AppConfig.orderService");
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }

    // 나중에 정액할인->정률할인으로 바꾸게 되면 여기만 바꾸면 됨
    @Bean
    public DiscountPolicy discountPolicy() {
//        return new FixDiscountPolicy();
        return new RateDiscountPolicy();
    }
    
    // call 하는 부분을 추가했음
    
    /* 예상
    call AppConfig.memberService
    call AppConfig.memberRepository
    call AppConfig.memberRepository
    call AppConfig.orderService
    call AppConfig.memberRepository
     */
    
    /* 결과
    call AppConfig.memberService
    call AppConfig.memberRepository
    call AppConfig.orderService
     */ // 중복은 호출되지 않음 -> 싱글톤 유지
}
