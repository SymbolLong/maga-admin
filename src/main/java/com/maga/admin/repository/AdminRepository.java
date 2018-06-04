package com.maga.admin.repository;

import com.maga.admin.entity.Admin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    Admin findByAccessKeyAndToken(String accessKey, String token);

    Admin findByAccessKeyAndLoginName(String accessKey, String loginName);

    Page<Admin> findByAccessKey(String accessKey, Pageable pageable);

    Page<Admin> findByNameLikeOrLoginNameLike(String name, String loginName, Pageable pageable);
}
