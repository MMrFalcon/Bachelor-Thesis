package com.falcon.forum.model


import javax.validation.constraints.Size

class CommentsDTO {

    private Long id

    @Size(min = 5, max = 2000)
    private String commentMessage
    private Date created
    private Date updated
    private Long points
    private boolean isCorrect

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

    Long getPoints() {
        return points
    }

    void setPoints(Long points) {
        this.points = points
    }

    boolean getIsCorrect() {
        return isCorrect
    }

    void setIsCorrect(boolean isCorrect) {
        this.isCorrect = isCorrect
    }
}
