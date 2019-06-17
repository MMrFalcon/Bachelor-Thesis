package com.falcon.forum.model


import javax.persistence.Column
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size
import java.time.LocalDate

class UserDTO {

    Long id

    @NotNull
    @NotEmpty
    @Size(min = 5, max = 25)
    String username

    @NotNull
    @NotEmpty
    @Size(min = 9)
    String password

    @NotNull
    @NotEmpty
    @Size(min = 9)
    String passwordConfirmation

    @Email
    @NotNull
    @NotEmpty
    @Column(unique = true, name = "email")
    String email
    Long points
    Long plusPoints
    Long minusPoints
    boolean active

    LocalDate created
    LocalDate updated
}
