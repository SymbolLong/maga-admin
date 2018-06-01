package com.maga.admin.controller;

import com.alibaba.druid.util.StringUtils;
import com.maga.admin.entity.ApiResult;
import com.maga.admin.entity.Permission;
import com.maga.admin.service.PermissionService;
import com.maga.admin.util.ApiResultBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/permission")
public class PermissionController {
    @Autowired
    private PermissionService permissionService;

    @PostMapping
    public ApiResult create(@RequestParam String name, @RequestParam String value,
                            @RequestParam String accessKey, @RequestParam(required = false) String remark){

        Permission permission = permissionService.findByAccessKeyAndValue(accessKey, value);
        if (permission != null){
            return ApiResultBuilder.failure("权限已存在");
        }
        permission = new Permission();
        permission.setName(name);
        permission.setValue(value);
        permission.setAccessKey(accessKey);
        if (!StringUtils.isEmpty(remark)){
            permission.setRemark(remark);
        }
        permissionService.save(permission);
        return ApiResultBuilder.success("创建权限成功", permissionService.toJSONObject(permission));
    }

    @PutMapping("/{id}")
    public ApiResult update(@PathVariable Long id, @RequestParam String name, @RequestParam String value,
                            @RequestParam String accessKey, @RequestParam(required = false) String remark){
        Permission permission = permissionService.findById(id);
        if (permission == null) {
            return ApiResultBuilder.failure("权限不存在");
        }
        Permission checkPermission = permissionService.findByAccessKeyAndValue(accessKey, value);
        if (checkPermission != null && !checkPermission.getId().equals(id)) {
            return ApiResultBuilder.failure("客户权限已存在");
        }
        permission.setUpdateDate(new Date());
        permission.setName(name);
        permission.setValue(value);
        permission.setAccessKey(accessKey);
        if (!StringUtils.isEmpty(remark)) {
            permission.setRemark(remark);
        }
        permissionService.save(permission);
        return ApiResultBuilder.success("修改权限成功", permissionService.toJSONObject(permission));
    }

    @DeleteMapping("/{id}")
    public ApiResult delete(@PathVariable Long id) {
        Permission permission = permissionService.findById(id);
        if (permission == null) {
            return ApiResultBuilder.failure("权限不存在");
        }
        permissionService.delete(id);
        return ApiResultBuilder.success("删除权限成功");
    }

    @GetMapping("/{id}")
    public ApiResult findById(@PathVariable Long id) {
        Permission permission = permissionService.findById(id);
        if (permission == null) {
            return ApiResultBuilder.failure("权限不存在");
        }
        return ApiResultBuilder.success("查询权限成功", permissionService.toJSONObject(permission));
    }

    @GetMapping("/accessKey")
    public ApiResult findByAccessKey(@RequestParam String accessKey, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        Page<Permission> permissions = permissionService.findByAccessKey(accessKey, page, size);
        return ApiResultBuilder.success("查询条目成功", permissionService.toJSONPage(permissions));
    }

    @GetMapping("/list")
    public ApiResult list(@RequestParam String name, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        Page<Permission> permissions = permissionService.findByName(name, page, size);
        return ApiResultBuilder.success("查询条目成功", permissionService.toJSONPage(permissions));
    }

}
