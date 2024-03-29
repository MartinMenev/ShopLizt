package com.example.myshoppingapp.repository;

import com.example.myshoppingapp.model.enums.Category;
import com.example.myshoppingapp.model.recipes.Recipe;
import com.example.myshoppingapp.model.users.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long>,
        JpaSpecificationExecutor<Recipe> {


    Optional<Recipe> getRecipeById(Long id);


    List<Recipe> findAllByAuthorOrderByIdDesc(UserEntity author);

    Optional<List<Recipe>> findAllByCategory(Category category);

    @Query(value = "select * from recipes as r where locate(:text, r.name) > 0 "+
        "or locate(:text, r.description) > 0", nativeQuery = true)
    Optional<List<Recipe>> findAllContainingSearchText(String text);

    Optional<List<Recipe>> findAllByAuthor(UserEntity userEntity);


}
