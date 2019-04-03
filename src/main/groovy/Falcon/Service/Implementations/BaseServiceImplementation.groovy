package Falcon.Service.Implementations

import Falcon.Exceptions.InactiveEntityException
import Falcon.Persist.BaseEntity
import Falcon.Service.BaseService
import org.springframework.data.jpa.repository.JpaRepository

import java.util.stream.Collectors

abstract class BaseServiceImplementation <T extends BaseEntity, K extends Serializable, R extends JpaRepository<T, K>>
        implements BaseService<T, K, R> {

    abstract R getRepository()


    @Override
    T save(T entity) {
        return getRepository().save(entity)
    }

    @Override
    T saveAndFlush(T entity) {
        return getRepository().saveAndFlush(entity)
    }


    @Override
    T delete(K id) {
        T entity = getOne(id)
        entity.setActive(false)
        return save(entity)
    }

    @Override
    Collection<T> getAll() {
        Closure active = {BaseEntity baseEntity -> baseEntity.isActive()}
        return getRepository().findAll().stream()
                .filter{BaseEntity baseEntity -> baseEntity.isActive()}.collect(Collectors.toList())
    }

    @Override
    T getOne(K id) {
        T entity = getRepository().getOne(id)
        if(entity.isActive()) {
            return entity
        }else{
            throw new InactiveEntityException("You try to modify inactive entity!") //TESTME
        }
    }
}
