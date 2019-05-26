package com.falcon.forum.model

class PostDTO {

    Long id
    String title
    String content
    Date created
    Date updated
    Long points
    boolean resolved

    PostDTO(){}

    PostDTO(String title, String content) {
        this.title = title
        this.content = content
    }

}
