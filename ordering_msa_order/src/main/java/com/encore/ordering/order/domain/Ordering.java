package com.encore.ordering.order.domain;

import com.encore.ordering.common.BaseTimeEntity;
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
    private Long memberId;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.ORDERED;

    @OneToMany(mappedBy = "ordering", cascade = CascadeType.PERSIST)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Builder
    public Ordering(Long memberId){
        this.memberId = memberId;
    }

    public void cancelOrder(){
        this.orderStatus = OrderStatus.CANCELED;
    }

}
