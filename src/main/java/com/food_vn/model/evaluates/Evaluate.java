package com.food_vn.model.evaluates;

import com.food_vn.lib.base_model.BaseModel;
import com.food_vn.model.users.User;
import jakarta.persistence.*;

@Entity
public class Evaluate extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @Column(nullable = false)
    private int score;

    @Column(nullable = false)
    private String comment;

    public Evaluate(Long id, User user, int score, String comment) {
        this.id = id;
        this.user = user;
        this.score = score;
        this.comment = comment;
    }

    public Evaluate() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
