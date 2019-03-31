package Falcon.Persist

import groovy.transform.Canonical
import groovy.transform.EqualsAndHashCode

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@EqualsAndHashCode(callSuper = true)
@Canonical
@Entity
@Table(name="post")
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
    @Column
    private String authorName

    private String tags

    String getTitle() { title }
    String getContent() { content }
    String getAuthorName() { this.authorName }

    void setTitle(String title) { this.title = title }
    void setContent(String content) { this.content = content }
    void setAuthorName(authorName) { this.authorName = authorName}
    }
