package com.maga.admin.repository;

import com.maga.admin.entity.Permission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    Permission findByAccessKeyAndValue(String accessKey, String value);

    Page<Permission> findByAccessKey(String accessKey, Pageable pageable);

    Page<Permission> findByNameLike(String name, Pageable pageable);
}
