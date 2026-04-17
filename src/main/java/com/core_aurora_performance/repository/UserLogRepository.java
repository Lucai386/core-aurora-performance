package com.core_aurora_performance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.core_aurora_performance.model.UserLog;

@Repository
public interface UserLogRepository extends JpaRepository<UserLog, Long> {

    List<UserLog> findByUserIdOrderByCreatedAtDesc(Integer userId);
}
