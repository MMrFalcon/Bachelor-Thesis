package Falcon.Persist

import groovy.transform.Canonical
import groovy.transform.EqualsAndHashCode

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table
import javax.validation.constraints.NotNull

@EqualsAndHashCode(callSuper = true)
@Canonical
@Entity
@Table(name="tags")
class Tags extends BaseEntity {

    @Column
    @NotNull
    private Long postId

    @Column
    @NotNull
    private String tag


    String getTag() { tag }
    void setTag(String tag) { this.tag = tag}


    Long getPostId() { postId }
    void setPostId(Long postId) { this.postId = postId }

}
