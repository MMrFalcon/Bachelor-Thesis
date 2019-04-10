package Falcon.Persist

import javax.persistence.*

@MappedSuperclass
@Inheritance
class BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id

    @Column(name="active")
    private boolean active

    @Column(name="created_date", updatable = false)
    private Date createdDate

    @Column(name="updated_date")
    private Date updatedDate

    @PrePersist
    protected void loadDataBeforeInsert(){
        this.createdDate = new Date()
        this.updatedDate = new Date()
        this.active = true
    }


    void setActive(boolean active) {this.active = active}
    boolean isActive() { active }

    void setUpdatedDate(Date updatedDate) { this.updatedDate = updatedDate}

    Long getId() { id }

    Date getCreatedDate() { createdDate }
    Date getUpdatedDate() { updatedDate }
}
