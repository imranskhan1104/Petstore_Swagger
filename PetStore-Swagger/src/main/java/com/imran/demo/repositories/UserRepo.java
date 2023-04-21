package com.imran.demo.repositories;

import com.imran.demo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepo extends JpaRepository<User, String> {
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM users u WHERE u.user_name = ?1", nativeQuery = true)
    void deleteByUsername(@Param("userName") String userName);

    @Transactional
    @Query(value = "select * from users u where u.user_name = ?1", nativeQuery = true)
    public User findByUserName(String userName);
}
