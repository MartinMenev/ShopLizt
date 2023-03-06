package com.example.myshoppingapp.repository;

import com.example.myshoppingapp.domain.recipes.Recipe;
import com.example.myshoppingapp.domain.users.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {



    Optional<UserEntity> findByEmail (String email);

    Optional<UserEntity> findUserEntityByUsername (String username);


    Optional<List<UserEntity>> findAllByFavoriteRecipesContains(Recipe recipe);
}
