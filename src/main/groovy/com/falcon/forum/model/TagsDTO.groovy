package com.falcon.forum.model

import javax.validation.constraints.Size

class TagsDTO {

    private Long id
    @Size(min=2)
    private String tag
    private Date created
    private Date updated

    TagsDTO(){}

    TagsDTO(String tag) {
        this.tag =tag
    }

    Date getCreated() {
        return created
    }

    void setCreated(Date created) {
        this.created = created
    }

    Date getUpdated() {
        return updated
    }

    void setUpdated(Date updated) {
        this.updated = updated
    }

    String getTag() { tag }
    void setTag(String tag) { this.tag = tag}

    Long getId() { id }
    void setId(Long id) { this.id = id }

}
