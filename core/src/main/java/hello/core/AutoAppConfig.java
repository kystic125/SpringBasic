package hello.core;

import hello.core.member.MemberRepository;
import hello.core.member.MemoryMemberRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(
//        basePackages = "hello.core.member", // 탐색할 패키지의 시작 위치 지정 가능 -> hello.core.member 하위만 탐색
//        basePackageClasses = AutoAppConfig.class, // 내 패키지 (hello.core)부터 탐색
        // 지정하지 않을 시 @ComponentScan이 붙은 설정 정보 클래스의 패키지가 시작 위치
        // -> AppConfig 를 프로젝트 최상단에 두면 안적어도 됨!
        // AppConfig 를 제외하기 위해 작성 (기존의 AppConfig 유지한 채 하기 때문에..)
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class)
)
public class AutoAppConfig {
    @Bean(name = "memoryMemberRepository")
    MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }
}

