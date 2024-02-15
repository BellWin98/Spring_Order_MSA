package com.encore.ordering.item.controller;

import com.encore.ordering.common.CommonResponse;
import com.encore.ordering.item.domain.Item;
import com.encore.ordering.item.dto.request.ItemRequest;
import com.encore.ordering.item.dto.request.SearchItemRequest;
import com.encore.ordering.item.dto.response.ItemResponse;
import com.encore.ordering.item.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ItemController {
    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping("/item/create")
    // 이미지 파일을 받아와야 하므로, multipart를 사용해야함. JSON이 아닌 form-data 활용 (@RequestBody 미사용)
    public ResponseEntity<CommonResponse> create(ItemRequest itemRequest){
        Item item = itemService.create(itemRequest);
        return new ResponseEntity<>(new CommonResponse(HttpStatus.CREATED,
                "Item Successfully Created", item.getId()), HttpStatus.CREATED);
    }

    @GetMapping("/items")
    // Page size default: 20, 0번 페이지부터 시작
    public ResponseEntity<List<ItemResponse>> findItems(SearchItemRequest searchItemRequest, Pageable pageable){
        List<ItemResponse> itemResponses = itemService.findItems(searchItemRequest, pageable);
        return new ResponseEntity<>(itemResponses, HttpStatus.OK);
    }

    @GetMapping("/item/{id}/image")
        public ResponseEntity<Resource> findImage(@PathVariable Long id){
            Resource resource = itemService.findImage(id);
            HttpHeaders headers = new HttpHeaders(); // 파일의 타입을 스프링에 알려주기 위함
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    @PatchMapping("/item/{id}/update")
    public ResponseEntity<CommonResponse> update(@PathVariable Long id, ItemRequest itemRequest){
        Item item = itemService.update(id, itemRequest);
        return new ResponseEntity<>(new CommonResponse(HttpStatus.OK,
                "Item Successfully Updated", item.getId()), HttpStatus.OK);
    }

    @DeleteMapping("/item/{id}/delete")
    public ResponseEntity<CommonResponse> delete(@PathVariable Long id){
        Item item = itemService.delete(id);
        return new ResponseEntity<>(new CommonResponse(HttpStatus.OK,
                "Item Successfully Deleted", item.getId()), HttpStatus.OK);
    }
}
