package com.encore.ordering.order.service;

import com.encore.ordering.item.domain.Item;
import com.encore.ordering.item.repository.ItemRepository;
import com.encore.ordering.member.domain.Member;
import com.encore.ordering.member.repository.MemberRepository;
import com.encore.ordering.order.domain.OrderStatus;
import com.encore.ordering.order.domain.Ordering;
import com.encore.ordering.order.dto.request.OrderRequest;
import com.encore.ordering.order.dto.response.OrderResponse;
import com.encore.ordering.order.repository.OrderRepository;
import com.encore.ordering.order_item.domain.OrderItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, MemberRepository memberRepository, ItemRepository itemRepository) {
        this.orderRepository = orderRepository;
        this.memberRepository = memberRepository;
        this.itemRepository = itemRepository;
    }

    public Ordering create(List<OrderRequest> orderRequests){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member findMember = memberRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new EntityNotFoundException("Member Email Not Found"));

        if (orderRequests.isEmpty()){
            throw new IllegalArgumentException("선택한 상품이 없습니다.");
        }

        Ordering ordering = Ordering.builder().member(findMember).build();

        // Ordering 객체가 생성될 때 Cascade.Persist에 의해 OrderItem 객체도 함께 생성
        for (OrderRequest orderRequest : orderRequests){
            Item findItem = itemRepository.findById(orderRequest.getItemId())
                    .orElseThrow(() -> new EntityNotFoundException("Item Not Found"));
            OrderItem orderItem = OrderItem.builder()
                    .quantity(orderRequest.getCount())
                    .ordering(ordering)
                    .item(findItem)
                    .build();
            ordering.getOrderItems().add(orderItem);
            if (findItem.getStockQuantity() - orderRequest.getCount() < 0){
                throw new IllegalArgumentException("상품 재고가 부족합니다.");
            }
            orderItem.getItem().updateStockQuantity(
                    findItem.getStockQuantity() - orderRequest.getCount());
        }
        return orderRepository.save(ordering);
    }

    public Ordering cancel(Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Ordering findOrder = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 주문이 없습니다."));
        if (!(authentication.getAuthorities().contains((new SimpleGrantedAuthority("ROLE_ADMIN"))) ||
                findOrder.getMember().getEmail().equals(email))){
            throw new AccessDeniedException("접근 권한이 없습니다.");
        }
        if (findOrder.getOrderStatus() == OrderStatus.CANCELED){
            throw new IllegalArgumentException("이미 취소된 주문입니다.");
        }
        findOrder.cancelOrder();
        for (OrderItem orderItem : findOrder.getOrderItems()){
            orderItem.getItem().updateStockQuantity(
                    orderItem.getItem().getStockQuantity() + orderItem.getQuantity());
        }
        return findOrder;
    }

    public List<OrderResponse> findOrders() {
        List<Ordering> orderings = orderRepository.findAll();
        return orderings.stream().map(OrderResponse::from).collect(Collectors.toList());
    }

    public List<OrderResponse> findMemberOrders(Long id){
        List<Ordering> orderings = orderRepository.findByMemberId(id);
        return orderings.stream().map(OrderResponse::from).collect(Collectors.toList());
    }

    public List<OrderResponse> findMyOrders() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member findMember = memberRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new EntityNotFoundException("해당 이메일의 회원이 존재하지 않습니다."));
        return findMember.getOrderings().stream().map(OrderResponse::from).collect(Collectors.toList());
    }
}
