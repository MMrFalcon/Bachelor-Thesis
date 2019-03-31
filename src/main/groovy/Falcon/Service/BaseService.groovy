package Falcon.Service

import Falcon.Persist.BaseEntity
import org.springframework.data.jpa.repository.JpaRepository


interface BaseService <T extends BaseEntity, K extends Serializable, R extends JpaRepository<T, K>> {

    R getRepository()

    T save(T entity)

    T saveAndFlush(T entity)

    T delete(K id)

    Collection<T> getAll()

    T getOne(K id)

}