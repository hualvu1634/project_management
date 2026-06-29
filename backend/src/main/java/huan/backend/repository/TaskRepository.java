package huan.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import huan.backend.entity.Task;
@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {

    Page<Task> findByProjectId(Long projectId, Pageable pageable);
    Page<Task> findByAssigneeId(Long assigneeId, Pageable pageable);
    
}
