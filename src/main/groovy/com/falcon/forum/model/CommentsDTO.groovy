package com.falcon.forum.model


import javax.validation.constraints.Size
import java.time.LocalDate

class CommentsDTO {

    Long id

    @Size(min = 5, max = 2000)
    String commentMessage
    LocalDate created
    LocalDate updated
    Long points
    boolean isCorrect
    boolean active

}
