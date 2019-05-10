package com.falcon.forum.model

import groovy.transform.Canonical

import javax.persistence.Column
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Canonical
class UserDTO {

    private Long id

    @NotNull
    @NotEmpty
    @Size(min=5, max=25)
    private String username

    @NotNull
    @NotEmpty
    @Size(min=9, max=25)
    private String password

    @Email
    @NotNull
    @NotEmpty
    @Column(unique = true, name="email")
    private String email
    private Long points
    private boolean active

    boolean getActive() {
        return active
    }

    Long getId() { id }
    String getPassword() { password }
    String getUsername() { username }
    String getEmail(){ email }
    Long getPoints(){ points }
    boolean isActive() { active }


    void setEmail(String email) { this.email = email }
    void setId(Long id) {this.id = id}
    void setUsername(String username) {this.username = username}
    void setPassword(String password) {this.password = password}
    void setActive(boolean active) {this.active = active}
    void setEnabled(boolean enabled) { this.enabled = enabled }
    void setPoints(Long points) {this.points = points}

}
