package com.example.myshoppingapp.service;

import com.example.myshoppingapp.model.comments.Comment;
import com.example.myshoppingapp.model.comments.dto.InputCommentDTO;
import com.example.myshoppingapp.model.comments.dto.OutputCommentDTO;
import com.example.myshoppingapp.model.recipes.Recipe;
import com.example.myshoppingapp.model.users.UserEntity;
import com.example.myshoppingapp.repository.CommentRepository;
import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Getter
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final RecipeService recipeService;


    @Autowired
    @Lazy
    public CommentService(CommentRepository commentRepository, ModelMapper modelMapper, UserService userService, RecipeService recipeService) {
        this.commentRepository = commentRepository;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.recipeService = recipeService;

    }


    public Comment addComment(InputCommentDTO inputCommentDTO, Long recipeId, String loggedName ) {
        Comment comment = modelMapper.map(inputCommentDTO, Comment.class);
        UserEntity author = this.userService.getLoggedUser(loggedName);
        Optional<Recipe> recipe = this.recipeService.getRecipeEntityById(recipeId);
        comment.setRecipe(recipe.get());
            comment.setAuthor(author);
            this.commentRepository.saveAndFlush(comment);
        return comment;
    }

    public List<OutputCommentDTO> showAllComments(Long recipeId) {
        return this.commentRepository
                .findAllByRecipeIdOrderByIdDesc(recipeId)
                .orElseThrow(NoSuchElementException::new)
                .stream()
                .map(comment -> this.modelMapper.map(comment, OutputCommentDTO.class))
                .toList();
    }
    public List<OutputCommentDTO> showLatestComments(Long recipeId) {
        return showAllComments(recipeId)
                .stream()
                .limit(3)
                .toList();
    }

    public List<OutputCommentDTO> showTopRatedComments() {
        return this.commentRepository
                .findAll()
                .stream()
                .map(comment -> modelMapper.map(comment, OutputCommentDTO.class))
                .sorted((a, b) -> b.getRating().compareTo(a.getRating()))
                .limit(4)
                .toList();
    }

    public List<OutputCommentDTO> getAllUnapprovedComments() {
        return this.commentRepository
                .findAll()
                .stream()
                .filter(comment -> !comment.isApproved())
                .map(comment -> modelMapper.map(comment, OutputCommentDTO.class))
                .sorted(Comparator.comparing(OutputCommentDTO::getId))
                .toList();
    }

    @Transactional
    @Modifying
    public Comment approveComment(long id) {
        Comment comment = this.commentRepository.getReferenceById(id);
        comment.setApproved();
        this.commentRepository.saveAndFlush(comment);
        return comment;
    }

    public void deleteCommentsByRecipeId(Long recipeId) {
       Optional<List<Comment>> comments = this.commentRepository.findAllByRecipeId(recipeId);
        comments.ifPresent(this.commentRepository::deleteAll);

    }

    @Transactional
    @Modifying
    public void deleteComment(long id, String loggedName) {
        Comment comment = this.commentRepository.getReferenceById(id);
        UserEntity loggedUser = userService.getLoggedUser(loggedName);
        if (Objects.equals(loggedUser.getId(), comment.getAuthor().getId()) || loggedUser.isAdmin()) {
            this.commentRepository.deleteById(id);
        }
    }
}
