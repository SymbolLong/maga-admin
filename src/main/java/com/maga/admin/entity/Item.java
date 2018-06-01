package com.maga.admin.entity;

import javax.persistence.Entity;
import java.io.Serializable;

/**
 * 数据条目
 *
 * @author zhangsl
 */
@Entity
public class Item extends BaseEntity implements Serializable {

    private String name;
    private String value;
    private String remark;
    private String accessKey;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }
}
