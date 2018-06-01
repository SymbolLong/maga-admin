package com.maga.admin.service;

import com.maga.admin.entity.Role;
import net.sf.json.JSONObject;
import org.springframework.data.domain.Page;

public interface RoleService {

    Role save(Role role, String permissions);

    void delete(Long id);

    Role findById(Long id);

    Role findByAccessKeyAndName(String accessKey, String name);

    Page<Role> findByAccessKey(String accessKey, int page, int size);

    Page<Role> findByName(String name, int page, int size);

    JSONObject toJSONObject(Role role);

    JSONObject toJSONPage(Page<Role> roles);

}
