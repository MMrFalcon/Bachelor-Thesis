package com.falcon.forum.model


import javax.persistence.Column
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

class UserDTO {

    Long id

    @NotNull
    @NotEmpty
    @Size(min = 5, max = 25)
    String username

    @NotNull
    @NotEmpty
    @Size(min = 9, max = 25)
    String password

    @Email
    @NotNull
    @NotEmpty
    @Column(unique = true, name = "email")
    String email
    Long points
    Long plusPoints
    Long minusPoints
    boolean active
}
