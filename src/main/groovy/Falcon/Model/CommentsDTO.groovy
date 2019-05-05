package Falcon.Model

class CommentsDTO {

    private Long id
    private String commentMessage
    private Date created
    private Date updated

    Long getId() {
        return id
    }

    void setId(Long id) {
        this.id = id
    }

    String getCommentMessage() {
        return commentMessage
    }

    void setCommentMessage(String commentMessage) {
        this.commentMessage = commentMessage
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
}
