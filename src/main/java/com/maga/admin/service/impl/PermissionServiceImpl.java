package com.maga.admin.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.maga.admin.entity.Item;
import com.maga.admin.entity.Permission;
import com.maga.admin.repository.PermissionRepository;
import com.maga.admin.service.PermissionService;
import com.maga.admin.util.DateUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    public Permission save(Permission permission) {
        return permissionRepository.saveAndFlush(permission);
    }

    @Override
    public void delete(Long id) {
        permissionRepository.deleteById(id);
    }

    @Override
    public Permission findById(Long id) {
        Optional<Permission> permission = permissionRepository.findById(id);
        return permission.isPresent() ? permission.get() : null;
    }

    @Override
    public Permission findByAccessKeyAndValue(String accessKey, String value) {
        return permissionRepository.findByAccessKeyAndValue(accessKey, value);
    }

    @Override
    public Page<Permission> findByAccessKey(String accessKey, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return permissionRepository.findByAccessKey(accessKey, pageRequest);
    }

    @Override
    public Page<Permission> findByName(String name, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        if (StringUtils.isEmpty(name)) {
            name = "%%";
        } else {
            name = "%" + name + "%";
        }
        return permissionRepository.findByNameLike(name, pageRequest);
    }

    @Override
    public JSONObject toJSONObject(Permission permission) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", permission.getId());
        jsonObject.put("name", permission.getName());
        jsonObject.put("value", permission.getValue());
        jsonObject.put("accessKey", permission.getAccessKey());
        jsonObject.put("remark", permission.getRemark());
        jsonObject.put("createDate", DateUtil.getStrFromDate(permission.getCreateDate()));
        return jsonObject;
    }

    @Override
    public JSONObject toJSONPage(Page<Permission> permissions) {
        JSONArray array = new JSONArray();
        for (Permission permission : permissions.getContent()) {
            array.add(toJSONObject(permission));
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("page", permissions.getNumber() + 1);
        jsonObject.put("size", permissions.getSize());
        jsonObject.put("total", permissions.getTotalElements());
        jsonObject.put("permissions", array);
        return jsonObject;
    }
}
