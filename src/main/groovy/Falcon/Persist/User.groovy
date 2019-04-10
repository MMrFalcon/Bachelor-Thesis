package Falcon.Persist


import groovy.transform.EqualsAndHashCode

import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@EqualsAndHashCode(callSuper = true)
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

    @OneToMany(mappedBy = "user")
    private Set<Post> posts = new HashSet<>()


    String getPassword() { password }
    String getUsername() { username }
    String getEmail(){ email }
    Long getPoints(){ points }



    void setEmail(String email) { this.email = email }
    void setUsername(String username) {this.username = username}
    void setPassword(String password) {this.password = password}
    void setPoints(Long points) {this.points = points}

    Set<Post> getPosts() {
        return posts
    }

    void setPosts(Set<Post> posts) {
        this.posts = posts
    }

    @PrePersist
    void setPointsAtStart(){
        if (this.points == 0 || this.points == null)
            this.points = 0
    }

}
