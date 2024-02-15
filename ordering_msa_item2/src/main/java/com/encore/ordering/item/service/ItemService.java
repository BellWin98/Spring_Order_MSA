package com.encore.ordering.item.service;

import com.encore.ordering.item.domain.Item;
import com.encore.ordering.item.dto.request.ItemRequest;
import com.encore.ordering.item.dto.request.SearchItemRequest;
import com.encore.ordering.item.dto.response.ItemResponse;
import com.encore.ordering.item.repository.ItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class ItemService {
    private final ItemRepository itemRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Item create(ItemRequest itemRequest) {
        Item item = itemRepository.save(Item.toEntity(itemRequest));
        if (itemRequest.getItemImage() != null){
            log.info("이미지 추가");
            MultipartFile multipartFile = itemRequest.getItemImage();
            String fileName = multipartFile.getOriginalFilename(); // 확장자 포함한 파일명 추출
            Path path = Paths.get("C:/Users/Playdata/OneDrive/바탕 화면/tmp/", item.getId() + "_" + fileName);
            item.setImagePath(path.toString());
            try {
                byte[] bytes = multipartFile.getBytes(); // 이미지 파일을 바이트로 변환
                // 해당 경로의 폴더에 이미지 파일 추가. 이미 동일 파일이 있으면 덮어 쓰기(Write), 없으면 Create
                Files.write(path, bytes, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            } catch (IOException e) {
                throw new IllegalArgumentException("Image Not Available");
            }
        }
        return item;
    }

    public Item delete(Long id) {
        Item findItem = itemRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Item Not Found"));
        findItem.deleteItem();
        return findItem;
    }

    public Resource findImage(Long id) {
        Item findItem = itemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item Not Found"));
        String imagePath = findItem.getImagePath();
        Path path = Paths.get(imagePath);
        Resource resource;
        try {
            resource = new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Url Form Is Not Valid");
        }
        return resource;
    }
    public Item update(Long id, ItemRequest itemRequest) {
        Item findItem = itemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item Not Found"));
        MultipartFile multipartFile = itemRequest.getItemImage();
        String fileName = multipartFile.getOriginalFilename(); // 확장자 포함한 파일명 추출
        Path path = Paths.get("C:/Users/Playdata/OneDrive/바탕 화면/tmp/", findItem.getId() + "_" + fileName);
        findItem.updateItem(itemRequest.getName(), itemRequest.getCategory(), itemRequest.getPrice(),
                itemRequest.getStockQuantity(), path.toString());
        try {
            byte[] bytes = multipartFile.getBytes(); // 이미지 파일을 바이트로 변환
            // 해당 경로의 폴더에 이미지 파일 추가. 이미 동일 파일이 있으면 덮어 쓰기(Write), 없으면 Create
            Files.write(path, bytes, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        } catch (IOException e) {
            throw new IllegalArgumentException("Image Not Available");
        }
        return findItem;

    }

    public List<ItemResponse> findItems(SearchItemRequest searchItemRequest, Pageable pageable) {
        // 검색을 위해 Specification 객체 사용
        // Specification 객체는 복잡한 쿼리를 명세를 이용해 정의하여 쉽게 생성
        Specification<Item> spec = new Specification<Item>() {
            @Override
            // root: 엔티티의 속성에 접근하기 위한 객체, builder는 쿼리를 생성하기 위한 객체
            public Predicate toPredicate(Root<Item> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                if (searchItemRequest.getName() != null){
                    predicateList.add(criteriaBuilder.like(root.get("name"),
                            "%" + searchItemRequest.getName() + "%"));
                }
                if (searchItemRequest.getCategory() != null){
                    predicateList.add(criteriaBuilder.like(root.get("category"),
                            "%" + searchItemRequest.getCategory() + "%"));
                }
                predicateList.add(criteriaBuilder.equal(root.get("delYn"), "N"));
                Predicate[] predicateArr = new Predicate[predicateList.size()];
                for (int i = 0; i < predicateArr.length; i++){
                    predicateArr[i] = predicateList.get(i);
                }
                Predicate predicate = criteriaBuilder.and(predicateArr);
                return predicate;
            }
        };
        Page<Item> items = itemRepository.findAll(spec, pageable);
        List<Item> itemList = items.getContent();
        return itemList.stream()
                .map(item -> ItemResponse.from(item))
                .collect(Collectors.toList());
    }
}
