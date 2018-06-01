package com.maga.admin.repository;

import com.maga.admin.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    Item findByAccessKeyAndValue(String accessKey, String value);

    Page<Item> findByNameLike(String name, Pageable pageable);

    Page<Item> findByAccessKey(String accessKey, Pageable pageable);
}
