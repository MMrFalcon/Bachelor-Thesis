package Falcon.Model

import groovy.transform.Canonical

@Canonical
class UserDTO {

    private Long id
    private String username
    private String password
    private String email
    private Long points
    private boolean active

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
