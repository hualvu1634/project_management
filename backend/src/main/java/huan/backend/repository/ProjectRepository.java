package huan.backend.repository;

import huan.backend.entity.Project;


import org.springframework.data.jpa.repository.JpaRepository;


public interface ProjectRepository extends JpaRepository<Project, Long> {
}