package com.maga.admin.service;

import com.maga.admin.entity.Permission;
import net.sf.json.JSONObject;
import org.springframework.data.domain.Page;

public interface PermissionService {

    Permission save(Permission permission);

    void delete(Long id);

    Permission findById(Long id);

    Permission findByAccessKeyAndValue(String accessKey, String value);

    Page<Permission> findByAccessKey(String accessKey, int page, int size);

    Page<Permission> findByName(String name, int page, int size);

    JSONObject toJSONObject(Permission permission);

    JSONObject toJSONPage(Page<Permission> permissions);

}
