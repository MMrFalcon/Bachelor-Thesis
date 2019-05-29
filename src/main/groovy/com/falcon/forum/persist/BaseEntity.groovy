package com.falcon.forum.persist

import javax.persistence.*
import java.time.LocalDate

@MappedSuperclass
@Inheritance
class BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id

    @Column(name="active")
    private boolean active

    @Column(name="created_date", updatable = false)
    private LocalDate createdDate

    @Column(name="updated_date")
    private LocalDate updatedDate

    @PrePersist
    protected void loadDataBeforeInsert(){
        this.createdDate = LocalDate.now()
        this.updatedDate = LocalDate.now()
        this.active = true
    }


    void setActive(boolean active) {this.active = active}
    boolean isActive() { active }

    void setUpdatedDate(LocalDate updatedDate) { this.updatedDate = updatedDate}

    void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate
    }

    Long getId() { id }
    Long setId(Long id) {this.id = id}

    LocalDate getCreatedDate() { createdDate }
    LocalDate getUpdatedDate() { updatedDate }
}
