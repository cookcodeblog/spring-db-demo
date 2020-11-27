package cn.xdevops.spring.repository;

import cn.xdevops.spring.entiy.Fruit;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FruitRepository extends CrudRepository<Fruit, Long> {
}
