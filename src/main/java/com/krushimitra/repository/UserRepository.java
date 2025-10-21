package com.krushimitra.repository;

import com.krushimitra.entity.User;
import com.krushimitra.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
    
    List<User> findByRole(UserRole role);
    
    List<User> findByRoleAndIsApproved(UserRole role, Boolean isApproved);
    
    @Query("SELECT u FROM User u WHERE u.role = 'EXPERT' AND u.isApproved = true ORDER BY u.rating DESC")
    List<User> findTopExperts();
    
    @Query("SELECT u FROM User u WHERE u.role = 'EXPERT' AND u.expertise LIKE %:expertise% AND u.isApproved = true")
    List<User> findExpertsByExpertise(@Param("expertise") String expertise);
}
