package huan.backend.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import huan.backend.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("SELECT COUNT(m) > 0 FROM Member m WHERE m.project.id = :projectId AND m.user.id = :userId AND m.isActive = true")
    boolean checkActiveMember(@Param("projectId") Long projectId, @Param("userId") Long userId);

    @Query("SELECT m FROM Member m WHERE m.project.id = :projectId AND m.isActive = true")
    Page<Member> findActiveByProject(@Param("projectId") Long projectId, Pageable pageable);

    @Query("SELECT m FROM Member m WHERE m.user.id = :userId AND m.isActive = true")
    List<Member> findActiveByUser(@Param("userId") Long userId);

}