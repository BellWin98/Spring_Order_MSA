package com.encore.ordering.order.controller;

import com.encore.ordering.common.CommonResponse;
import com.encore.ordering.order.domain.Ordering;
import com.encore.ordering.order.dto.request.OrderRequest;
import com.encore.ordering.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/order/create")
    public ResponseEntity<CommonResponse> create(@RequestBody List<OrderRequest> orderRequests){
            Ordering ordering = orderService.create(orderRequests);
        return new ResponseEntity<>(new CommonResponse(HttpStatus.CREATED,
                "Order Successfully Created", ordering.getId()), HttpStatus.CREATED);
    }

//    @DeleteMapping("/order/{id}/cancel")
//    public ResponseEntity<CommonResponse> cancel(@PathVariable Long id) {
//        Ordering ordering = orderService.cancel(id);
//        return new ResponseEntity<>(new CommonResponse(HttpStatus.OK,
//                "Order Successfully Canceled", ordering.getId()), HttpStatus.OK);
//    }

    @GetMapping("/orders")
    public ResponseEntity<CommonResponse> findOrders(){
        return new ResponseEntity<>(new CommonResponse(HttpStatus.OK,
                "Orders Successfully Found", orderService.findOrders()), HttpStatus.OK);
    }
}
