package Falcon.Persist

import groovy.transform.Canonical
import groovy.transform.EqualsAndHashCode

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.PrePersist
import javax.persistence.Table
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@EqualsAndHashCode(callSuper = true)
@Canonical
@Entity
@Table(name="users")
class User extends BaseEntity {

    @Column(unique = true, name="username")
    @NotNull
    @NotEmpty
    private String username

    @NotNull
    @NotEmpty
    @Column(name="password")
    private String password

    @Email
    @NotNull
    @NotEmpty
    @Column(unique = true, name="email")
    private String email

    @Column(name="points")
    private Long points


    String getPassword() { password }
    String getUsername() { username }
    String getEmail(){ email }
    Long getPoints(){ points }



    void setEmail(String email) { this.email = email }
    void setUsername(String username) {this.username = username}
    void setPassword(String password) {this.password = password}
    void setPoints(Long points) {this.points = points}

    @PrePersist
    void setPointsAtStart(){
        if (this.points == 0 || this.points == null)
            this.points = 0
    }

}
