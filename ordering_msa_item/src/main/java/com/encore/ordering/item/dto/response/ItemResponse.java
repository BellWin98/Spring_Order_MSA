package com.encore.ordering.item.dto.response;

import com.encore.ordering.item.domain.Item;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ItemResponse {
    private Long id;
    private String name;
    private String category;
    private int price;
    private int stockQuantity;
    private String imagePath;

    public static ItemResponse from(Item item){
        return ItemResponse.builder()
                .id(item.getId())
                .name(item.getName())
                .price(item.getPrice())
                .category(item.getCategory())
                .stockQuantity(item.getStockQuantity())
                .imagePath(item.getImagePath())
                .build();
    }
}
