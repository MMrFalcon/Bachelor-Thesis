package com.falcon.forum.service.implementation

import com.falcon.forum.exception.InactiveEntityException
import com.falcon.forum.persist.BaseEntity
import com.falcon.forum.service.BaseService
import org.springframework.data.jpa.repository.JpaRepository

import javax.persistence.EntityNotFoundException
import java.util.stream.Collectors

abstract class BaseServiceImplementation <T extends BaseEntity, K extends Serializable, R extends JpaRepository<T, K>>
        implements BaseService<T, K, R> {

    private R repository

    void setRepository(R repository) {
        this.repository = repository
    }

    @Override
    T save(T entity) {
        return repository.save(entity)
    }

    @Override
    T saveAndFlush(T entity) {
        return repository.saveAndFlush(entity)
    }


    @Override
    T delete(K id) {
        T entity = getOne(id)
        entity.setActive(false)
        return save(entity)
    }

    @Override
    Collection<T> getAll() {
       Collection<T> collect = repository.findAll().stream()
               .filter{BaseEntity baseEntity -> baseEntity.isActive()}.collect(Collectors.toList())
        collect.sort{a,b -> a.createdDate<=>b.createdDate}
        return collect
    }

    @Override
    T getOne(K id) throws EntityNotFoundException {
        T entity = repository.getOne(id)
        if(entity.isActive()) {
            return entity
        }else{
            throw new InactiveEntityException("You are trying to find an inactive entity!")
        }
    }

}