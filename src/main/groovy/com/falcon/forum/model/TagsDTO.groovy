package com.falcon.forum.model

import javax.validation.constraints.Size

class TagsDTO {

    Long id
    @Size(min=2)
    String tag
    Date created
    Date updated

    TagsDTO(){}

    TagsDTO(String tag) {
        this.tag =tag
    }

}
