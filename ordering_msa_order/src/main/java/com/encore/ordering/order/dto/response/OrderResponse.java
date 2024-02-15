package com.encore.ordering.order.dto.response;

import com.encore.ordering.order.domain.Ordering;
import com.encore.ordering.order.domain.OrderItem;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 주문자 이메일
 * 주문 상태
 * 주문 시간
 */
@Getter
@Builder
public class OrderResponse {
    private Long orderId;
//    private String memberEmail;
    private String orderStatus;
    private List<OrderItemResponse> orderItems;

    @Getter
    @Builder
    public static class OrderItemResponse {
        private Long itemId;
        private String itemName;
        private int quantity;
    }

    public static OrderResponse from(Ordering ordering){
        List<OrderItemResponse> orderItemResponses = new ArrayList<>();
        for (OrderItem orderItem : ordering.getOrderItems()){
            OrderItemResponse orderItemResponse = OrderItemResponse.builder()
                    .itemId(orderItem.getId())
//                    .itemName(orderItem.getItem().getName())
                    .quantity(orderItem.getQuantity())
                    .build();
            orderItemResponses.add(orderItemResponse);
        }

        return OrderResponse.builder()
                .orderId(ordering.getId())
//                .memberEmail(ordering.getMember().getEmail())
                .orderStatus(ordering.getOrderStatus().toString())
                .orderItems(orderItemResponses)
                .build();
    }
}
