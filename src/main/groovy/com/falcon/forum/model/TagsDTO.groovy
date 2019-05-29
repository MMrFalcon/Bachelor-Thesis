package com.falcon.forum.model

import javax.validation.constraints.Size
import java.time.LocalDate

class TagsDTO {

    Long id
    @Size(min=2)
    String tag
    LocalDate created
    LocalDate updated
    boolean active

    TagsDTO(){}

    TagsDTO(String tag) {
        this.tag =tag
    }

}
