package huan.backend.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import huan.backend.entity.User;




public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
  
}
