package hello.core.order;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import hello.core.member.MemoryMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor // final이 전부 포함된 생성자를 만들어 줌
public class OrderServiceImpl implements OrderService {

    private final MemberRepository memberRepository;
//    private final DiscountPolicy discountPolicy = new FixDiscountPolicy();
//    private final DiscountPolicy discountPolicy = new RateDiscountPolicy();
    // 고정할인 -> 정률할인 변경을 위해선 OrderServiceImpl 의 해당 부분을 변경해야 한다
    // DIP 위반 (인터페이스(추상)에만 의존하지 않고 구체 클래스에도 함께 의존하기 때문)
    // 따라서 FixDiscount -> RateDiscount 변경 시 코드의 변경이 필요함 (OCP 위반)
    // 이 문제를 어떻게 해결할까? (-> 아래처럼 인터페이스에만 의존하도록 설계 변경)
    private final DiscountPolicy discountPolicy;
    // 그러나 위 방법처럼 하면 구현체가 없는데 어떻게 실행될까...? (지금은 NullPointException 발생)
    // -> 누군가가 OrderServiceImpl에 DiscountPolicy의 구현 객체를 대신 생성하고 주입해주면 어떨까?
    // AppConfig는 생성한 객체의 참조를 "생성자"를 통해 주입해준다.
    
    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(member, itemPrice);

        return new Order(memberId, itemName, itemPrice, discountPrice);
    }

    /**
 * 1. 타입 매충
 * 2. 파라미터 명 매칭
    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }
 * -> 위 코드에서 DiscountPolicy rateDiscountPolicy와 this.discountPolicy = rateDiscountPolicy로 했다면
 * 기존에는 DiscountPolicy 밑에 Fix와 Rate 2개가 있어서 오류가 발생했지만
 * rateDiscountPolicy를 자동으로 찾아가서 오류가 해결됨
*/

/**
    public OrderServiceImpl(MemberRepository memberRepository, @Qualifier("mainDiscountPolicy") DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }
 * @Qualifier를 추가하면 매칭되는 부분을 실행한다
 * 만약 찾지 못한다면? (즉, @Qualifier("mainDiscountPollicy")가 없다면
 * mainDiscountPolicy라는 스프링 빈을 추가로 찾는다 (그래도 없으면 예외)
 * -> 명확한 용도로 사용하도록 주의
*/

/** 추천!!
 * @primary 사용 (@Primary가 우선순위로 동작한다)
 */

    // 테스트 용도
    public MemberRepository getMemberRepository() {
        return memberRepository;
    }

    // 다양한 의존관계 주입 방법
    // (final을 지우고 해야 실행됨!)

    // 필드 주입 (비권장 -> 값을 바꿀 수 있는 방법이 없음 -> 별도의 setter를 열어 줘야 함)
/*
     @Autowired private MemberRepository memberRepository;
     @Autowired private DiscountPolicy discountPolicy;
     처럼 할 수도 있음
*/

    // 생성자 주입 (이 경우 위에 final을 없애야 함)
/*
   // @Autowired가 없으면 생성자로 자동 연결 // 이게 있으면 생성자가 없어도 됨
    @Autowired
    public void setMemberRepository(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Autowired
    public void setDiscountPolicy(DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
    }
*/

    // 일반 메서드 주입
/*
    @Autowired
    public void init(MemberRepository memberRepository, DiscountPolicy, discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }
    처럼 사용 가능 (일반 메소드 위에 @Autowired를 작성한 형태)
 */
}
