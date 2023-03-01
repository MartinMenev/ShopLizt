package com.example.myshoppingapp.repository;

import com.example.myshoppingapp.domain.recipes.OutputRecipeDTO;
import com.example.myshoppingapp.domain.recipes.Recipe;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class RecipeSpecification implements Specification<Recipe> {

    private final OutputRecipeDTO outputRecipeDTO;

    public RecipeSpecification(OutputRecipeDTO outputRecipeDTO) {
        this.outputRecipeDTO = outputRecipeDTO;
    }

    @Override
    public Predicate toPredicate(Root<Recipe> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Predicate p = cb.conjunction();

        if (outputRecipeDTO.getId() != null) {
            p.getExpressions().add(
                    cb.and(cb.equal(root.get("id"), outputRecipeDTO.getId()))
            );
        }


        return p;
    }
}
