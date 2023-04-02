package com.example.myshoppingapp.repository;

import com.example.myshoppingapp.model.recipes.Recipe;
import com.example.myshoppingapp.model.users.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail (String email);

    Optional<UserEntity> findUserEntityByUsername (String username);


    Optional<List<UserEntity>> findAllByFavoriteRecipesContains(Recipe recipe);

    @Query(value = "select * from users as u where locate(:text, u.username) > 0", nativeQuery = true)
    Optional<List<UserEntity>> findAllContainingSearchText(String text);

}
