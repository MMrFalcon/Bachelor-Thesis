package com.falcon.forum.model


import javax.validation.constraints.Size

class CommentsDTO {

    Long id

    @Size(min = 5, max = 2000)
    String commentMessage
    Date created
    Date updated
    Long points
    boolean isCorrect

}
