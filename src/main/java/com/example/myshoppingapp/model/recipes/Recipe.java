package com.example.myshoppingapp.model.recipes;

import com.example.myshoppingapp.model.BaseEntity;
import com.example.myshoppingapp.model.comments.Comment;
import com.example.myshoppingapp.model.enums.Category;
import com.example.myshoppingapp.model.pictures.Picture;
import com.example.myshoppingapp.model.products.Product;
import com.example.myshoppingapp.model.users.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@Entity
@Table(name = "recipes")
public class Recipe extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String url;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Column
    private LocalDate dateAdded;

   @Column(scale = 1, precision = 2)
    private double rating;

   @ElementCollection
   private List<Double> ratingList;

   @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
   @JoinColumn
   private UserEntity author;

   @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.REMOVE, CascadeType.MERGE, CascadeType.PERSIST})
   @JoinColumn
   private List<Picture> pictureList;

   @Column(columnDefinition = "TEXT")
   private String imageUrl;

   @OneToMany(targetEntity = Comment.class, mappedBy= "recipe",
           cascade = {CascadeType.REMOVE})
   @LazyCollection(LazyCollectionOption.FALSE)
   private List<Comment> commentList;

  @ManyToMany (cascade = CascadeType.DETACH)
  @LazyCollection(LazyCollectionOption.FALSE)
   private List<Product> productList;

  @Column
  private long numberOfSaves;

    @Column
    private long position;

  @ManyToMany
  private List<UserEntity> fan;


  public Recipe() {
      this.dateAdded = LocalDate.now();
      this.commentList = new ArrayList<>();
      this.productList = new ArrayList<>();
      this.ratingList = new ArrayList<>();
      this.pictureList = new ArrayList<>();
      this.numberOfSaves = 0;
  }

  public boolean hasImageUrl(){
      return this.imageUrl != null;
  }

  public void addRating(double currentRating) {
    this.ratingList.add(currentRating);
    this.rating = ratingList
            .stream()
            .mapToDouble(Double::doubleValue)
            .average().orElse(0);
//      double sum = 0;
//      for (Double rating : ratingList) {
//          sum += rating;
//          this.rating = sum / ratingList.size();
//      }

  }

    public Recipe setName(String name) {
        this.name = name;
        return this;
    }

    public Recipe setUrl(String url) {
        this.url = url;
        return this;
    }

    public Recipe setDescription(String description) {
        this.description = description;
        return this;
    }

    public Recipe setCategory(Category category) {
        this.category = category;
        return this;
    }

    public Recipe setRating(double rating) {
        this.rating = rating;
        return this;
    }

    public Recipe setAuthor(UserEntity author) {
        this.author = author;
        return this;
    }

    public Recipe addPicture(Picture picture) {
        this.pictureList.add(picture);
        return this;
    }

    public Recipe setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public void setNumberOfSaves(long numberOfSaves) {
        this.numberOfSaves = numberOfSaves;
    }
}
