package com.encore.ordering.item.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ItemRequest {
    private String name;
    private String category;
    private int price;
    private int stockQuantity;
    private MultipartFile itemImage;
}
