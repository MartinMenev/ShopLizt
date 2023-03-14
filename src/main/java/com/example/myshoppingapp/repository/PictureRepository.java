package com.example.myshoppingapp.repository;

import com.example.myshoppingapp.model.pictures.Picture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PictureRepository extends JpaRepository<Picture, Long> {

    Optional<Picture> findByAuthorId(Long id);

    List<Picture> findAll();


}
