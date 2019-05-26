package com.falcon.forum.persist


import groovy.transform.EqualsAndHashCode

import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "users")
class User extends BaseEntity {

    @Column(unique = true, name = "username")
    @NotNull
    @NotEmpty
    @Size(min = 5, max = 25)
    private String username

    @NotNull
    @NotEmpty
    @Column(name = "password")
    @Size(min = 9, max = 25)
    private String password

    @Email
    @NotNull
    @NotEmpty
    @Column(unique = true, name = "email")
    private String email

    @Column(name = "points")
    private Long points

    private Long plusPoints

    private Long minusPoints

    @OneToMany(mappedBy = "user")
    private Set<Post> posts = new HashSet<>()

    @OneToMany(mappedBy = "user")
    private Set<Comments> comments = new HashSet<>()

    @ManyToMany(mappedBy = "postUsersVotes", cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    private Set<Post> votedPosts = new HashSet<>()

    @ManyToMany(mappedBy = "answerUsersVotes", cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    private Set<Comments> votedComments = new HashSet<>()

    Long getPlusPoints() {
        return plusPoints
    }

    void setPlusPoints(Long plusPoints) {
        this.plusPoints = plusPoints
    }

    Long getMinusPoints() {
        return minusPoints
    }

    void setMinusPoints(Long minusPoints) {
        this.minusPoints = minusPoints
    }

    Set<Comments> getVotedComments() {
        return votedComments
    }

    void setVotedComments(Set<Comments> votedComments) {
        this.votedComments = votedComments
    }

    Set<Post> getVotedPosts() {
        return votedPosts
    }

    void setVotedPosts(Set<Post> votedPosts) {
        this.votedPosts = votedPosts
    }

    Set<Comments> getComments() {
        return comments
    }

    void setComments(Set<Comments> comments) {
        this.comments = comments
    }

    String getPassword() { password }

    String getUsername() { username }

    String getEmail() { email }

    Long getPoints() { points }


    void setEmail(String email) { this.email = email }

    void setUsername(String username) { this.username = username }

    void setPassword(String password) { this.password = password }

    void setPoints(Long points) { this.points = points }

    Set<Post> getPosts() {
        return posts
    }

    void setPosts(Set<Post> posts) {
        this.posts = posts
    }

    @PrePersist
    void setPointsAtStart() {
        this.points = 0
        this.plusPoints = 0
        this.minusPoints = 0
    }

}
