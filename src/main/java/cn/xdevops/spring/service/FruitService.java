package cn.xdevops.spring.service;

import cn.xdevops.spring.dto.FruitDto;

import java.util.List;

public interface FruitService {
    List<FruitDto> fruitList();

    FruitDto save(FruitDto fruitDto);

    FruitDto findById(Long id);

    FruitDto updateFruit(long id, FruitDto fruitDto);

    void deleteById(Long id);
}
