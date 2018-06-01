package com.maga.admin.repository;

import com.maga.admin.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByAccessKeyAndName(String accessKey, String name);

    Page<Role> findByAccessKey(String accessKey, Pageable pageable);

    Page<Role> findByNameLike(String name, Pageable pageable);
}
