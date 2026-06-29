package huan.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import huan.backend.entity.TaskLog;
@Repository
public interface TaskLogRepository extends JpaRepository<TaskLog,Long> {
    
}
