package com.booleanuk.cohorts.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties("users")
    private User user;

    @CreationTimestamp
    @Column
    private OffsetDateTime createdAt;

    @OneToMany(mappedBy = "post")
    @JsonIgnore
    private Set<Like> likes = new HashSet<>();

    @OneToMany(mappedBy = "post")
    @JsonIgnore
    private Set<Comment> comments = new HashSet<>();

    @Transient
    private String firstName;

    @Transient
    private String lastName;

    @Transient
    public String getFirstName() {
//        if (firstName != null && !firstName.isBlank()) return firstName;
        if(user != null && user.getProfile() != null){
            return user.getProfile().getFirstName();
        }
        return null;
    }

    @Transient
    public String getLastName() {
//        if (lastName != null && !lastName.isBlank()) return lastName;
        if(user != null && user.getProfile() != null){
            return user.getProfile().getLastName();
        }
        return null;
    }

    @Transient
    public String getInitials(){
        String firstInitial = getFirstName().substring(0, 1).toUpperCase();
        String secondInitial = getLastName().substring(0, 1).toUpperCase();

        return (firstInitial + secondInitial);
    }

    @Transient
    public int getLikesCount() {
        return this.getLikes() == null ? 0 : this.getLikes().size();
    }

    @Transient
    public int getCommentsCount() {
        return this.getComments() == null ? 0 : this.getComments().size();
    }

    public Post(int id) {
        this.id = id;
    }

    public Post(User user, String content) {
        this.user = user;
        this.content = content;
    }

}
