package Falcon.Persist

import groovy.transform.Canonical
import groovy.transform.EqualsAndHashCode

import javax.persistence.*
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
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "post")
    Set<Tags> tags = new HashSet<>()

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

    }
