package com.falcon.forum.service


import com.falcon.forum.persist.BaseEntity
import org.springframework.data.jpa.repository.JpaRepository


interface BaseService<T extends BaseEntity, K extends Serializable, R extends JpaRepository<T, K>> {

    T save(T entity)

    T saveAndFlush(T entity)

    T delete(K id)

    Collection<T> getAll()

    T getOne(K id)

}