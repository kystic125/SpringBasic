package hello.core.order;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import hello.core.member.MemoryMemberRepository;

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

    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(member, itemPrice);

        return new Order(memberId, itemName, itemPrice, discountPrice);
    }
    
    // 테스트 용도
    public MemberRepository getMemberRepository() {
        return memberRepository;
    }
}
