package com.encore.ordering.order.domain;

import com.encore.ordering.common.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ordering_id", nullable = false)
    private Ordering ordering;

    @JoinColumn(name = "item_id", nullable = false)
    private Long itemId;

    @Builder
    public OrderItem(int quantity, Ordering ordering, Long itemId){
        this.quantity = quantity;
        this.ordering = ordering;
        this.itemId = itemId;
    }
}
