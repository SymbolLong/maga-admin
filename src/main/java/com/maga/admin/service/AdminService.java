package com.maga.admin.service;

import com.maga.admin.entity.Admin;
import com.maga.admin.entity.ApiResult;
import com.maga.admin.entity.LoginRecord;
import net.sf.json.JSONObject;
import org.springframework.data.domain.Page;

public interface AdminService {

    Admin findByAccessKeyAndLoginName(String accessKey, String loginName);

    Admin findByAccessKeyAndToken(String accessKey, String token);

    Admin save(Admin admin);

    JSONObject toJSONObject(Admin admin);

    Admin findById(Long id);

    void delete(Long id);

    Page<Admin> findByAccessKey(String accessKey, int page, int size);

    JSONObject toJSONPage(Page<Admin> admins);

    Page<Admin> findByName(String name, int page, int size);

    JSONObject login(Admin admin, String ip);

    void logout(Admin admin);

    Page<LoginRecord> findByAdminId(Long adminId, int page, int size);

    ApiResult checkPermission(String token, String permission);
}
