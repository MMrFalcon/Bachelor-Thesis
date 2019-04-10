package Falcon.Persist


import groovy.transform.EqualsAndHashCode

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.ManyToMany
import javax.persistence.Table
import javax.validation.constraints.NotNull

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name="tags")
class Tags extends BaseEntity {

    @Column
    @NotNull
    private String tag

    @ManyToMany(mappedBy = "tags")
//    private List<Post> posts
    private Set<Post> posts

    Tags() {}

    Set<Post> getPosts() {
        return posts
    }

    void setPosts(Set<Post> posts) {
        this.posts = posts
    }
//    List<Post> getPosts() {
//        return posts
//    }
//
//    void setPosts(List<Post> posts) {
//        this.posts = posts
//    }

    String getTag() { tag }
    void setTag(String tag) { this.tag = tag}

}
