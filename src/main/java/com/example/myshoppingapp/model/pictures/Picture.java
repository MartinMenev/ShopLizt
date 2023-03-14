package com.example.myshoppingapp.model.pictures;

import com.example.myshoppingapp.model.BaseEntity;
import com.example.myshoppingapp.model.users.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "pictures")
public class Picture extends BaseEntity {

    @Column(columnDefinition = "TEXT")
    private String pictureUrl;
    @OneToOne (fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn()
    private UserEntity author;


    public Picture setPictureUrl(String url) {
        this.pictureUrl = url;
        return this;
    }

    public Picture setAuthor(UserEntity author) {
        this.author = author;
        return this;
    }
}
