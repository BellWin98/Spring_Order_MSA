package com.encore.ordering.order.domain;

import com.encore.ordering.common.BaseTimeEntity;
import com.encore.ordering.member.domain.Member;
import com.encore.ordering.order_item.domain.OrderItem;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ordering extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.ORDERED;

    @OneToMany(mappedBy = "ordering", cascade = CascadeType.PERSIST)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Builder
    public Ordering(Member member){
        this.member = member;
    }

    public void cancelOrder(){
        this.orderStatus = OrderStatus.CANCELED;
    }

}
