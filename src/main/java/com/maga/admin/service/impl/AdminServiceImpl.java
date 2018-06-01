package com.maga.admin.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.maga.admin.entity.Admin;
import com.maga.admin.entity.LoginRecord;
import com.maga.admin.entity.Role;
import com.maga.admin.repository.AdminRepository;
import com.maga.admin.repository.LoginRecordRepository;
import com.maga.admin.service.AdminService;
import com.maga.admin.service.RoleService;
import com.maga.admin.util.DateUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {

    private static String TOKEN_KEY = "D34DABE743454FE2B403CDE605B5532D";

    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private LoginRecordRepository loginRecordRepository;
    @Autowired
    private RoleService roleService;

    @Override
    public Admin findByAccessKeyAndLoginName(String accessKey, String loginName) {
        return adminRepository.findByAccessKeyAndLoginName(accessKey, loginName);
    }

    @Override
    public Admin save(Admin admin) {
        return adminRepository.saveAndFlush(admin);
    }

    @Override
    public JSONObject toJSONObject(Admin admin) {
        String roleName = "";
        if (admin.getRoleId() != null) {
            Role role = roleService.findById(admin.getRoleId());
            roleName = role.getName();
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", admin.getId());
        jsonObject.put("name", admin.getName());
        jsonObject.put("avatar", admin.getAvatar());
        jsonObject.put("loginName", admin.getLoginName());
        jsonObject.put("roleName", roleName);
        jsonObject.put("accessKey", admin.getAccessKey());
        jsonObject.put("remark", admin.getRemark());
        return jsonObject;
    }

    @Override
    public Admin findById(Long id) {
        Optional<Admin> admin = adminRepository.findById(id);
        return admin.isPresent() ? admin.get() : null;
    }

    @Override
    public void delete(Long id) {
        adminRepository.deleteById(id);
    }

    @Override
    public Page<Admin> findByAccessKey(String accessKey, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return adminRepository.findByAccessKey(accessKey, pageRequest);
    }

    @Override
    public JSONObject toJSONPage(Page<Admin> admins) {
        JSONArray array = new JSONArray();
        for (Admin admin : admins.getContent()) {
            array.add(toJSONObject(admin));
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("page", admins.getNumber());
        jsonObject.put("size", admins.getSize());
        jsonObject.put("total", admins.getTotalElements());
        jsonObject.put("admins", array);
        return jsonObject;
    }

    @Override
    public Page<Admin> findByName(String name, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        if (StringUtils.isEmpty(name)) {
            name = "%%";
        } else {
            name = "%" + name + "%";
        }
        return adminRepository.findByNameLikeOOrLoginNameLike(name, name, pageRequest);
    }

    @Override
    public JSONObject login(Admin admin, String ip) {
        Date expire = new DateTime().plusHours(2).toDate();
        String token = Jwts.builder()
                .setSubject(admin.getName())
                .signWith(SignatureAlgorithm.HS512, TOKEN_KEY)
                .setExpiration(expire)
                .compact();
        admin.setToken(token);
        save(admin);

        LoginRecord record = new LoginRecord();
        record.setAdminId(admin.getId());
        record.setIp(ip);
        loginRecordRepository.saveAndFlush(record);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token", token);
        jsonObject.put("expire", DateUtil.getStrFromDate(expire));
        jsonObject.put("name", admin.getName());
        jsonObject.put("avatar", admin.getAvatar());
        return jsonObject;
    }

    @Override
    public Page<LoginRecord> findByAdminId(Long adminId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return loginRecordRepository.findByAdminId(adminId, pageRequest);
    }
}
