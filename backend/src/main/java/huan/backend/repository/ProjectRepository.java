package huan.backend.repository;

import huan.backend.entity.Project;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    // Tìm các dự án đang hoạt động
    Page<Project> findAllByIsActiveTrue(Pageable pageable);
}