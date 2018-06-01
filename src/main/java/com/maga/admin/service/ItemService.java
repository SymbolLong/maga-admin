package com.maga.admin.service;

import com.maga.admin.entity.Item;
import net.sf.json.JSONObject;
import org.springframework.data.domain.Page;

public interface ItemService {

    Item save(Item item);

    Item findById(Long id);

    Item findByAccessKeyAndValue(String accessKey, String value);

    void delete(Long id);

    Page<Item> findByAccessKey(String accessKey, int page, int size);

    Page<Item> findByName(String name, int page, int size);

    JSONObject toJSONObject(Item item);

    JSONObject toJSONPage(Page<Item> items);
}
