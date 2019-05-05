package Falcon.Model

import javax.validation.constraints.Size

class TagsDTO {

    private Long id
    @Size(min=2)
    private String tag

    TagsDTO(){}

    TagsDTO(String tag) {
        this.tag =tag
    }


    String getTag() { tag }
    void setTag(String tag) { this.tag = tag}

    Long getId() { id }
    void setId(Long id) { this.id = id }

}
