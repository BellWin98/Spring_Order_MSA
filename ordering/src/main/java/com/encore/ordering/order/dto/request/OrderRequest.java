package com.encore.ordering.order.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
//    private List<Long> itemIds;
//    private List<Integer> quantities;
//    private List<OrderItemRequest> orderItemRequests;
    private Long itemId;
    private int count;

//    @Data
//    public static class OrderItemRequest{
//        private Long itemId;
//        private int count;
//    }
}

/**
 * 1번 방법 예시 데이터
 * {
     "itemIds" : [1, 2], "quantities" : [10, 20]
 * }
 */

/**
 * 2번 방법 예시 데이터
 * {
 *     "orderItemRequests" : [
 *          {"itemId" : 1, "quantity" : 10},
 *          {"itemId" : 20, "quantity" : 20}
 *     ]
 * }
 */

/**
 * 최종적으로 받아올 데이터
 * [
 *      {"itemId" : 1, "quantity" : 10},
 *      {"itemId" : 20, "quantity" : 20}
 * ]
 */
