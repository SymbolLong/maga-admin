package com.maga.admin.entity;

import javax.persistence.Entity;
import java.io.Serializable;

@Entity
public class LoginRecord extends BaseEntity implements Serializable {

    private Long adminId;
    private String ip;

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
