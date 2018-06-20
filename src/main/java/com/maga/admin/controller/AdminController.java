package com.maga.admin.controller;

import com.alibaba.druid.util.StringUtils;
import com.maga.admin.entity.Admin;
import com.maga.admin.entity.ApiResult;
import com.maga.admin.service.AdminService;
import com.maga.admin.util.ApiResultBuilder;
import com.maga.admin.util.Md5Util;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping
    public ApiResult create(@RequestBody JSONObject jsonObject) {
        String loginName = jsonObject.getString("loginName");
        String accessKey = jsonObject.getString("accessKey");
        Admin admin = adminService.findByAccessKeyAndLoginName(accessKey, loginName);
        if (admin != null) {
            return ApiResultBuilder.failure("管理员已存在");
        }
        admin = new Admin();
        admin.setName(jsonObject.getString("name"));
        admin.setLoginName(loginName);
        admin.setPassword(Md5Util.MD5(jsonObject.getString("password")));
        admin.setAccessKey(accessKey);
        admin.setAvatar(jsonObject.getString("avatar"));
        if (jsonObject.containsKey("roleId") && !StringUtils.isEmpty(jsonObject.getString("roleId"))) {
            admin.setRoleId(jsonObject.getLong("roleId"));
        }
        adminService.save(admin);
        return ApiResultBuilder.success("创建管理员成功", adminService.toJSONObject(admin));
    }

    @PutMapping("/{id}")
    public ApiResult update(@PathVariable Long id, @RequestBody JSONObject jsonObject) {
        Admin admin = adminService.findById(id);
        if (admin == null) {
            return ApiResultBuilder.failure("管理员不存在");
        }
        String loginName = jsonObject.getString("loginName");
        String accessKey = jsonObject.getString("accessKey");
        Admin checkAdmin = adminService.findByAccessKeyAndLoginName(accessKey, loginName);
        if (checkAdmin != null && !checkAdmin.getId().equals(id)) {
            return ApiResultBuilder.failure("管理员已存在");
        }
        admin.setName(jsonObject.getString("name"));
        admin.setLoginName(loginName);
        if (jsonObject.containsKey("password") && !admin.getPassword().equals(jsonObject.getString("password"))) {
            admin.setPassword(Md5Util.MD5(jsonObject.getString("password")));
        }
        admin.setAccessKey(accessKey);
        admin.setAvatar(jsonObject.getString("avatar"));
        if (jsonObject.containsKey("roleId") && !StringUtils.isEmpty(jsonObject.getString("roleId"))) {
            admin.setRoleId(jsonObject.getLong("roleId"));
        }
        admin.setUpdateDate(new Date());
        adminService.save(admin);
        return ApiResultBuilder.success("修改管理员成功", adminService.toJSONObject(admin));
    }

    @DeleteMapping("/{id}")
    public ApiResult delete(@PathVariable Long id) {
        Admin admin = adminService.findById(id);
        if (admin == null) {
            return ApiResultBuilder.failure("管理员不存在");
        }
        adminService.delete(id);
        return ApiResultBuilder.success("删除管理员成功");
    }

    @GetMapping("/{id}")
    public ApiResult findById(@PathVariable Long id) {
        Admin admin = adminService.findById(id);
        if (admin == null) {
            return ApiResultBuilder.failure("管理员不存在");
        }
        return ApiResultBuilder.success("查询管理员成功", adminService.toJSONObject(admin));
    }

    @GetMapping("/accessKey")
    public ApiResult findByAccessKey(@RequestParam String accessKey, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        Page<Admin> admins = adminService.findByAccessKey(accessKey, page, size);
        return ApiResultBuilder.success("查询管理员成功", adminService.toJSONPage(admins));
    }

    @GetMapping("/list")
    public ApiResult list(@RequestParam String name, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        Page<Admin> admins = adminService.findByName(name, page, size);
        return ApiResultBuilder.success("查询管理员成功", adminService.toJSONPage(admins));
    }

    @GetMapping("/login")
    public ApiResult login(@RequestParam String loginName, @RequestParam String password,
                           @RequestParam String accessKey, @RequestParam String ip) {
        Admin admin = adminService.findByAccessKeyAndLoginName(accessKey, loginName);
        if (admin == null) {
            return ApiResultBuilder.failure("管理员不存在");
        }
        if (!admin.getPassword().equals(Md5Util.MD5(password))) {
            return ApiResultBuilder.failure("密码错误");
        }
        return ApiResultBuilder.success("管理员登录成功", adminService.login(admin, ip));
    }

    @GetMapping("/logout")
    public ApiResult logout(@RequestHeader String token, @RequestParam String accessKey) {
        Admin admin = adminService.findByAccessKeyAndToken(accessKey, token);
        if (admin == null) {
            return ApiResultBuilder.failure("管理员不存在");
        }
        adminService.logout(admin);
        return ApiResultBuilder.success("管理员退出成功");
    }

    @GetMapping("/checkPermission")
    public ApiResult checkPermission(@RequestHeader String token, @RequestParam String permission){
        return adminService.checkPermission(token, permission);
    }




}
