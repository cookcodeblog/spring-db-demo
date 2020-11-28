package cn.xdevops.spring.service;

import cn.xdevops.spring.entiy.Fruit;

import java.util.List;

public interface IFruitService {
  List<Fruit> fruitList();

  Fruit save(Fruit fruit);

  Fruit findById(Long id);

  Fruit updateFruit(long id, Fruit fruit);

  void deleteById(Long id);
}
