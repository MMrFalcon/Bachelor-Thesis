package com.falcon.forum.persist


import groovy.transform.EqualsAndHashCode

import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "post")
class Post extends BaseEntity {
    @NotNull
    @Size(min = 3, max = 40)
    @Column(unique = true)
    private String title

    @NotNull
    @Size(min = 5, max = 1000)
    @Column(unique = true)
    private String content

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user

    @ManyToMany
    @JoinTable(name = "post_tag", joinColumns = @JoinColumn(name = "post_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "tags_id", referencedColumnName = "id"))
    private Set<Tags> tags = new HashSet<>()

    @OneToMany(mappedBy = "post")
    private Set<Comments> comments = new HashSet<>()

    private Long points

    Set<Comments> getComments() {
        return comments
    }

    void setComments(Set<Comments> comments) {
        this.comments = comments
    }

    Set<Tags> getTags() {
        return tags
    }

    void setTags(Set<Tags> tags) {
        this.tags = tags
    }

    User getUser() {
        return user
    }

    void setUser(User user) {
        this.user = user
    }

    String getTitle() { title }

    String getContent() { content }

    void setTitle(String title) { this.title = title }

    void setContent(String content) { this.content = content }

    Long getPoints() {
        return points
    }

    void setPoints(Long points) {
        this.points = points
    }
}
