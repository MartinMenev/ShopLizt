package com.example.myshoppingapp.repository;

import com.example.myshoppingapp.domain.users.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {



    Optional<UserEntity> findByEmail (String email);

    Optional<UserEntity> findUserEntityByUsername (String username);

    @Modifying
    @Transactional
    @Query("update UserEntity u " +
            "set u.username = :newUsername, "+
            "u.password = :newPassword, " +
            "u.email = :newEmail " +
            "where u.id = :id"
    )
    void updateUser(Long id, String newUsername, String newPassword, String newEmail);

}
