package com.falcon.forum.persist

import groovy.transform.EqualsAndHashCode

import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "comment")
class Comments extends BaseEntity {
    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user

    @NotNull
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post

    @NotNull
    @Size(min = 5, max = 5000)
    @Column(unique = true)
    private String commentMessage

    @ManyToMany(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinTable(name = "comments_users_votes", joinColumns = @JoinColumn(name = "comment_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private Set<User> answerUsersVotes = new HashSet<>()

    private Long points

    private boolean isCorrect

    @PrePersist
    protected void loadPostDataBeforeInsert() {
        this.isCorrect = false
        this.points = 0L
    }

    Set<User> getAnswerUsersVotes() {
        return answerUsersVotes
    }

    void setAnswerUsersVotes(Set<User> answerUsersVotes) {
        this.answerUsersVotes = answerUsersVotes
    }

    User getUser() {
        return user
    }

    void setUser(User user) {
        this.user = user
    }

    Post getPost() {
        return post
    }

    void setPost(Post post) {
        this.post = post
    }

    String getCommentMessage() {
        return commentMessage
    }

    void setCommentMessage(String commentMessage) {
        this.commentMessage = commentMessage
    }

    Long getPoints() {
        return points
    }

    void setPoints(Long points) {
        this.points = points
    }

    boolean getIsCorrect() {
        return isCorrect
    }

    void setIsCorrect(boolean isCorrect) {
        this.isCorrect = isCorrect
    }
}
