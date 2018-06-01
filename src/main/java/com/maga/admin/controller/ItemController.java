package com.maga.admin.controller;

import com.alibaba.druid.util.StringUtils;
import com.maga.admin.entity.ApiResult;
import com.maga.admin.entity.Item;
import com.maga.admin.service.ItemService;
import com.maga.admin.util.ApiResultBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/item")
public class ItemController {
    @Autowired
    private ItemService itemService;

    @PostMapping
    public ApiResult create(@RequestParam String name, @RequestParam String value,
                            @RequestParam String accessKey, @RequestParam(required = false) String remark) {
        Item item = itemService.findByAccessKeyAndValue(accessKey, value);
        if (item != null) {
            return ApiResultBuilder.failure("条目已存在，不可创建");
        }
        item = new Item();
        item.setName(name);
        item.setValue(value);
        item.setAccessKey(accessKey);
        item.setRemark(remark);
        itemService.save(item);
        return ApiResultBuilder.success("创建条目成功", itemService.toJSONObject(item));
    }

    @PutMapping("/{id}")
    public ApiResult update(@PathVariable Long id, @RequestParam String name, @RequestParam String value,
                            @RequestParam String accessKey, @RequestParam(required = false) String remark) {

        Item item = itemService.findById(id);
        if (item == null) {
            return ApiResultBuilder.failure("条目不存在");
        }
        Item checkItem = itemService.findByAccessKeyAndValue(accessKey, value);
        if (checkItem != null && !checkItem.getId().equals(id)) {
            return ApiResultBuilder.failure("客户条目已存在");
        }
        item.setUpdateDate(new Date());
        item.setName(name);
        item.setValue(value);
        item.setAccessKey(accessKey);
        if (!StringUtils.isEmpty(remark)) {
            item.setRemark(remark);
        }
        itemService.save(item);
        return ApiResultBuilder.success("修改条目成功", itemService.toJSONObject(item));
    }

    @DeleteMapping("/{id}")
    public ApiResult delete(@PathVariable Long id) {
        Item item = itemService.findById(id);
        if (item == null) {
            return ApiResultBuilder.failure("条目不存在");
        }
        itemService.delete(id);
        return ApiResultBuilder.success("删除条目成功");
    }

    @GetMapping("/{id}")
    public ApiResult findById(@PathVariable Long id) {
        Item item = itemService.findById(id);
        if (item == null) {
            return ApiResultBuilder.failure("条目不存在");
        }
        return ApiResultBuilder.success("查询条目成功", itemService.toJSONObject(item));
    }

    @GetMapping("/accessKey")
    public ApiResult findByAccessKey(@RequestParam String accessKey, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        Page<Item> items = itemService.findByAccessKey(accessKey, page, size);
        return ApiResultBuilder.success("查询条目成功", itemService.toJSONPage(items));
    }


    @GetMapping("/list")
    public ApiResult list(@RequestParam String name, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        Page<Item> items = itemService.findByName(name, page, size);
        return ApiResultBuilder.success("查询条目成功", itemService.toJSONPage(items));
    }
}
