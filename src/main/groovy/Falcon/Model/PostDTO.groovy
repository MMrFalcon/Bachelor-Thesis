package Falcon.Model


import javax.persistence.Column
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

//JPA table
class PostDTO {

    private Long id

    @NotNull
    @Size(min = 3, max = 40)
    @Column(unique = true)
    private String title

    @NotNull
    @Size(min = 5, max = 1000)
    @Column(unique = true)
    private String content

    private Date created
    private Date updated

    private String authorName

    private String tags //musi byc zdefiniowane jako Set
                             //uzyte do skopiowania jako oddzielne entity TagsDTO


    PostDTO(){}

    PostDTO(String title, String content) {
        this.title = title
        this.content = content
    }

    String getTitle() {
        title
    }

    String getContent() {
        content
    }

    Long getId(){
        id
    }

    void setId(Long id) { this.id = id }
    void setTitle(String title) {
        this.title = title
    }

    void setContent(String content) {
        this.content = content
    }


    Date getCreated() {
        created
    }

    Date getUpdated() {
        updated
    }

    void setCreated(Date created) {
        this.created = created
    }

    void setUpdated(Date updated) {
        this.updated = updated
    }

    String getAuthorName() { this.authorName }

    void setAuthorName(authorName) { this.authorName = authorName }

    String getTags() { tags }
    void setTags(String tags) {this.tags = tags}

}
