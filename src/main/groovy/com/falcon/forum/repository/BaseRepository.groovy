package com.falcon.forum.repository


import com.falcon.forum.persist.BaseEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface BaseRepository <T extends BaseEntity, K extends Serializable>
        extends JpaRepository<T,K>, JpaSpecificationExecutor<T> {
}