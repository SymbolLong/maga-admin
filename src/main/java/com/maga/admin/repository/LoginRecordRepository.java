package com.maga.admin.repository;

import com.maga.admin.entity.LoginRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginRecordRepository extends JpaRepository<LoginRecord, Long> {

    Page<LoginRecord> findByAdminId(Long adminId, Pageable pageable);

}
