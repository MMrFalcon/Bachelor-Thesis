package Falcon.Persist

import groovy.transform.EqualsAndHashCode

import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name="comment")
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
    @Size(min = 5, max = 2000)
    private String commentMessage

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
}
