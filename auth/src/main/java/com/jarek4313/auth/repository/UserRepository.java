package com.jarek4313.auth.repository;

import com.jarek4313.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByLogin(String login);
    Optional<User> findUserByEmail(String email);
    Optional<User> findUserByUuid(String uuid);
    @Query(nativeQuery = true, value = "SELECT * FROM users where login=?1 and islock=false and isenabled=true")
    Optional<User> findUserByLoginAndLockAndEnabled(String username);

    @Query(nativeQuery = true, value = "SELECT * FROM users where login=?1 and islock=false and isenabled=true and role='ADMIN'")
    Optional<Object> findUserByLoginAndLockAndEnabledAndIsAdmin(String subject);
}
