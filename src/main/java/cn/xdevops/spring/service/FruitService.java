package cn.xdevops.spring.service;

import cn.xdevops.spring.vo.FruitVo;

import java.util.List;

public interface FruitService {
    List<FruitVo> fruitList();

    FruitVo save(FruitVo fruitVo);

    FruitVo findById(Long id);

    FruitVo updateFruit(long id, FruitVo fruitVo);

    void deleteById(Long id);
}
