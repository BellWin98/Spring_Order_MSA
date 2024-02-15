package com.encore.ordering.order_item.domain;

import com.encore.ordering.common.BaseTimeEntity;
import com.encore.ordering.item.domain.Item;
import com.encore.ordering.order.domain.Ordering;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Builder
    public OrderItem(int quantity, Ordering ordering, Item item){
        this.quantity = quantity;
        this.ordering = ordering;
        this.item = item;
    }
}
