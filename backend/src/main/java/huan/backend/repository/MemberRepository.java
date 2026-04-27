package huan.backend.repository;


import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import huan.backend.entity.Member;

public interface MemberRepository  extends JpaRepository<Member,Long>{

    boolean existsByProjectIdAndUserIdAndIsActiveTrue(Long id, Long id2);
    Optional<Member> findById(Long id);
    Page<Member> findByProjectIdAndIsActiveTrue(Long projectId, Pageable pageable);
    
}
