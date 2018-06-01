package com.maga.admin.controller;

import com.alibaba.druid.util.StringUtils;
import com.maga.admin.entity.ApiResult;
import com.maga.admin.entity.Role;
import com.maga.admin.service.RoleService;
import com.maga.admin.util.ApiResultBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping
    public ApiResult create(@RequestParam String name, @RequestParam String accessKey,
                            @RequestParam(required = false) String remark, @RequestParam(required = false) String permissions) {
        Role role = roleService.findByAccessKeyAndName(accessKey, name);
        if (role != null) {
            return ApiResultBuilder.failure("角色已存在");
        }
        role = new Role();
        role.setName(name);
        role.setAccessKey(accessKey);
        if (!StringUtils.isEmpty(remark)) {
            role.setRemark(remark);
        }
        roleService.save(role, permissions);
        return ApiResultBuilder.success("创建角色成功", roleService.toJSONObject(role));
    }

    @PutMapping("/{id}")
    public ApiResult update(@PathVariable Long id, @RequestParam String name, @RequestParam String accessKey,
                            @RequestParam(required = false) String remark, @RequestParam(required = false) String permissions) {
        Role role = roleService.findById(id);
        if (role == null) {
            return ApiResultBuilder.failure("角色不存在");
        }
        Role checkRole = roleService.findByAccessKeyAndName(accessKey, name);
        if (checkRole != null && !checkRole.getId().equals(id)) {
            return ApiResultBuilder.failure("角色已存在");
        }
        role.setName(name);
        role.setAccessKey(accessKey);
        if (!StringUtils.isEmpty(remark)) {
            role.setRemark(remark);
        }
        roleService.save(role, permissions);
        return ApiResultBuilder.success("修改角色成功", roleService.toJSONObject(role));
    }

    @DeleteMapping("/{id}")
    public ApiResult delete(@PathVariable Long id) {
        Role role = roleService.findById(id);
        if (role == null) {
            return ApiResultBuilder.failure("角色不存在");
        }
        roleService.save(role, "");
        roleService.delete(id);
        return ApiResultBuilder.success("删除角色成功");
    }

    @GetMapping("/{id}")
    public ApiResult findById(@PathVariable Long id) {
        Role role = roleService.findById(id);
        if (role == null) {
            return ApiResultBuilder.failure("角色不存在");
        }
        return ApiResultBuilder.success("修改角色成功", roleService.toJSONObject(role));
    }

    @GetMapping("/accessKey")
    public ApiResult findByAccessKey(@RequestParam String accessKey, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        Page<Role> roles = roleService.findByAccessKey(accessKey, page, size);
        return ApiResultBuilder.success("查询角色成功", roleService.toJSONPage(roles));
    }

    @GetMapping("/list")
    public ApiResult list(@RequestParam String name, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        Page<Role> roles = roleService.findByName(name, page, size);
        return ApiResultBuilder.success("查询角色成功", roleService.toJSONPage(roles));
    }

}
