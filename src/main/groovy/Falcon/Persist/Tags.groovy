package Falcon.Persist

import groovy.transform.Canonical
import groovy.transform.EqualsAndHashCode

import javax.persistence.*
import javax.validation.constraints.NotNull

@EqualsAndHashCode(callSuper = true)
@Canonical
@Entity
@Table(name="tags")
class Tags extends BaseEntity {

    @Column
    @NotNull
    private String tag

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post

    Post getPost() {
        return post
    }

    void setPost(Post post) {
        this.post = post
    }

    String getTag() { tag }
    void setTag(String tag) { this.tag = tag}

}
