package com.maga.admin.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.maga.admin.entity.Item;
import com.maga.admin.repository.ItemRepository;
import com.maga.admin.service.ItemService;
import com.maga.admin.util.DateUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ItemRepository itemRepository;

    @Override
    public Item save(Item item) {
        return itemRepository.saveAndFlush(item);
    }

    @Override
    public Item findById(Long id) {
        Optional<Item> item = itemRepository.findById(id);
        return item.isPresent() ? item.get() : null;
    }

    @Override
    public Item findByAccessKeyAndValue(String accessKey, String value) {
        return itemRepository.findByAccessKeyAndValue(accessKey, value);
    }

    @Override
    public void delete(Long id) {
        itemRepository.deleteById(id);
    }

    @Override
    public Page<Item> findByAccessKey(String accessKey, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return itemRepository.findByAccessKey(accessKey, pageRequest);
    }

    @Override
    public Page<Item> findByName(String name, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        if (StringUtils.isEmpty(name)) {
            name = "%%";
        } else {
            name = "%" + name + "%";
        }
        return itemRepository.findByNameLike(name, pageRequest);
    }

    @Override
    public JSONObject toJSONObject(Item item) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", item.getId());
        jsonObject.put("name", item.getName());
        jsonObject.put("value", item.getValue());
        jsonObject.put("accessKey", item.getAccessKey());
        jsonObject.put("createDate", DateUtil.getStrFromDate(item.getCreateDate()));
        jsonObject.put("remark", item.getRemark());
        return jsonObject;
    }

    @Override
    public JSONObject toJSONPage(Page<Item> items) {
        JSONArray array = new JSONArray();
        for (Item item : items.getContent()) {
            array.add(toJSONObject(item));
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("page", items.getNumber() + 1);
        jsonObject.put("size", items.getSize());
        jsonObject.put("total", items.getTotalElements());
        jsonObject.put("items", array);
        return jsonObject;
    }
}
