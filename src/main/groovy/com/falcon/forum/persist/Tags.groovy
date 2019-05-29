package com.falcon.forum.persist

import groovy.transform.EqualsAndHashCode

import javax.persistence.*
import javax.validation.constraints.NotNull

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name="tags")
class Tags extends BaseEntity {

    @Column
    @NotNull
    private String tag

    @ManyToMany(mappedBy = "tags", cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    private Set<Post> posts =  new HashSet<>()

    Tags() {}

    Set<Post> getPosts() {
        return posts
    }

    void setPosts(Set<Post> posts) {
        this.posts = posts
    }

    String getTag() { tag }
    void setTag(String tag) { this.tag = tag}

}
