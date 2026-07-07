package huan.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import huan.backend.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("SELECT t FROM Task t WHERE t.project.id = :projectId")
    Page<Task> findTasksByProject(@Param("projectId") Long projectId, Pageable pageable);

    @Query("SELECT t FROM Task t WHERE t.assignee.id = :assigneeId")
    Page<Task> findTasksByAssignee(@Param("assigneeId") Long assigneeId, Pageable pageable);
}