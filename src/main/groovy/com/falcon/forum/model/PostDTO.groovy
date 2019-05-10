package com.falcon.forum.model

class PostDTO {

    private Long id
    private String title
    private String content
    private Date created
    private Date updated
    private Long points

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

    Long getPoints() {
        return points
    }

    void setPoints(Long points) {
        this.points = points
    }
}
