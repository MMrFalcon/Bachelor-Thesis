package Falcon.Model

class TagsDTO {

    private Long id
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