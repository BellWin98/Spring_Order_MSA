package com.encore.ordering.item.domain;

import com.encore.ordering.common.BaseTimeEntity;
import com.encore.ordering.item.dto.request.ItemRequest;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Builder
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String category;
    private int price;
    private int stockQuantity;
    private String imagePath;
    @Builder.Default // 이걸 안쓰면 여기서 초기화 해도 초기 item 생성할 때 builder에서 Null값 들어감
    private String delYn = "N"; // 통상 String 자료형으로 사용

    public void deleteItem(){
        this.delYn = "Y";
    }
    public void updateStockQuantity(int newQuantity){
        this.stockQuantity = newQuantity;
    }
    public void setImagePath(String imagePath){
        this.imagePath = imagePath;
    }

    public void updateItem(String name,
                           String category,
                           int price,
                           int stockQuantity,
                           String imagePath){
        this.name = name;
        this.category = category;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.imagePath = imagePath;
    }

    public static Item toEntity(ItemRequest itemRequest){
        return Item.builder()
                .name(itemRequest.getName())
                .category(itemRequest.getCategory())
                .price(itemRequest.getPrice())
                .stockQuantity(itemRequest.getStockQuantity())
                .build();
    }
}
