package huan.backend.repository;


import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import huan.backend.entity.Member;

@Repository
public interface MemberRepository  extends JpaRepository<Member,Long>{

    boolean existsByProjectIdAndUserIdAndIsActiveTrue(Long id, Long id2);
      Page<Member> findByProjectIdAndIsActiveTrue(Long projectId,Pageable pageable);
    Page<Member> findByUserIdAndIsActiveTrue(Long userId, Pageable pageable);
    Optional<Member> findByUserId(Long userId);
    
}
