package com.maga.admin.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.maga.admin.entity.Permission;
import com.maga.admin.entity.Role;
import com.maga.admin.repository.PermissionRepository;
import com.maga.admin.repository.RoleRepository;
import com.maga.admin.service.PermissionService;
import com.maga.admin.service.RoleService;
import com.maga.admin.util.DateUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PermissionService permissionService;

    @Override
    public Role save(Role role, String permissions) {
        if (!StringUtils.isEmpty(permissions)) {
            Set<Permission> permissionSet = new HashSet<>();
            String[] ids = permissions.split(",");
            for (String id : ids) {
                Permission permission = permissionService.findById(Long.parseLong(id));
                if (permission != null && permission.getAccessKey().equals(role.getAccessKey())) {
                    permissionSet.add(permission);
                }
            }
            if (!permissionSet.isEmpty()) {
                role.setPermissions(permissionSet);
            }
        }
        return roleRepository.saveAndFlush(role);
    }

    @Override
    public void delete(Long id) {
        roleRepository.deleteById(id);
    }

    @Override
    public Role findById(Long id) {
        Optional<Role> role = roleRepository.findById(id);
        return role.isPresent() ? role.get() : null;
    }

    @Override
    public Role findByAccessKeyAndName(String accessKey, String name) {
        return roleRepository.findByAccessKeyAndName(accessKey, name);
    }

    @Override
    public Page<Role> findByAccessKey(String accessKey, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return roleRepository.findByAccessKey(accessKey, pageRequest);
    }

    @Override
    public Page<Role> findByName(String name, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        if (StringUtils.isEmpty(name)) {
            name = "%%";
        } else {
            name = "%" + name + "%";
        }
        return roleRepository.findByNameLike(name, pageRequest);
    }

    @Override
    public JSONObject toJSONObject(Role role) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", role.getId());
        jsonObject.put("name", role.getName());
        jsonObject.put("accessKey", role.getAccessKey());
        jsonObject.put("createDate", DateUtil.getStrFromDate(role.getCreateDate()));
        jsonObject.put("remark", role.getRemark());
        return jsonObject;
    }

    @Override
    public JSONObject toJSONPage(Page<Role> roles) {
        JSONArray array = new JSONArray();
        for (Role role : roles.getContent()) {
            array.add(toJSONObject(role));
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("page", roles.getNumber() + 1);
        jsonObject.put("size", roles.getSize());
        jsonObject.put("total", roles.getTotalElements());
        jsonObject.put("roles", array);
        return jsonObject;
    }
}
