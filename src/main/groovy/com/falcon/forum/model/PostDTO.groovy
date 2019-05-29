package com.falcon.forum.model

import java.time.LocalDate

class PostDTO {

    Long id
    String title
    String content
    LocalDate created
    LocalDate updated
    Long points
    boolean resolved
    boolean active

    PostDTO(){}

    PostDTO(String title, String content) {
        this.title = title
        this.content = content
    }

}
