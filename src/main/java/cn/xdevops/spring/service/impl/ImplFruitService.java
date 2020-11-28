package cn.xdevops.spring.service.impl;

import cn.xdevops.spring.entiy.Fruit;
import cn.xdevops.spring.repository.FruitRepository;
import cn.xdevops.spring.service.IFruitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class ImplFruitService implements IFruitService {

  @Autowired FruitRepository fruitRepository;

  @Override
  public List<Fruit> fruitList() {
    return StreamSupport.stream(fruitRepository.findAll().spliterator(), false)
        .collect(Collectors.toList());
  }

  @Override
  public Fruit save(Fruit fruit) {
    return fruitRepository.save(fruit);
  }

  @Override
  public Fruit findById(Long id) {
    return fruitRepository.findById(id).orElse(null);
  }

  @Override
  public Fruit updateFruit(long id, Fruit fruit) {
    fruit.setId(id);
    return fruitRepository.save(fruit);
  }

  @Override
  public void deleteById(Long id) {
    fruitRepository.deleteById(id);
  }
}
