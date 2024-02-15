package com.encore.ordering.item.dto.request;

import lombok.Data;

@Data
public class SearchItemRequest {
    private String name;
    private String category;
}
