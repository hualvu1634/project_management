package huan.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import huan.backend.entity.TaskLog;

public interface TaskLogRepository extends JpaRepository<TaskLog,Long> {
    
}
