package cn.xdevops.spring.service.impl;

import cn.xdevops.spring.vo.FruitVo;
import cn.xdevops.spring.entity.Fruit;
import cn.xdevops.spring.repository.FruitRepository;
import cn.xdevops.spring.service.FruitService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class FruitServiceImpl implements FruitService {

    @Autowired
    FruitRepository fruitRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<FruitVo> fruitList() {
        return StreamSupport.stream(fruitRepository.findAll().spliterator(), false)
                .map(x -> modelMapper.map(x, FruitVo.class))
                .collect(Collectors.toList());
    }

    @Override
    public FruitVo save(FruitVo fruitVo) {
        return modelMapper.map(fruitRepository.save(modelMapper.map(fruitVo, Fruit.class)), FruitVo.class);
    }

    @Override
    public FruitVo findById(Long id) {
        return fruitRepository.findById(id).map(x -> modelMapper.map(x, FruitVo.class)).orElse(null);
    }

    @Override
    public FruitVo updateFruit(long id, FruitVo fruitVo) {
        fruitVo.setId(id);
        return modelMapper.map(fruitRepository.save(modelMapper.map(fruitVo, Fruit.class)), FruitVo.class);
    }

    @Override
    public void deleteById(Long id) {
        fruitRepository.deleteById(id);
    }
}
