package cn.xdevops.spring.service.impl;

import cn.xdevops.spring.dto.FruitDto;
import cn.xdevops.spring.entiy.Fruit;
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
    public List<FruitDto> fruitList() {
        return StreamSupport.stream(fruitRepository.findAll().spliterator(), false)
                .map(x -> modelMapper.map(x, FruitDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public FruitDto save(FruitDto fruitDto) {
        return modelMapper.map(fruitRepository.save(modelMapper.map(fruitDto, Fruit.class)), FruitDto.class);
    }

    @Override
    public FruitDto findById(Long id) {
        return fruitRepository.findById(id).map(x -> modelMapper.map(x, FruitDto.class)).orElse(null);
    }

    @Override
    public FruitDto updateFruit(long id, FruitDto fruitDto) {
        fruitDto.setId(id);
        return modelMapper.map(fruitRepository.save(modelMapper.map(fruitDto, Fruit.class)), FruitDto.class);
    }

    @Override
    public void deleteById(Long id) {
        fruitRepository.deleteById(id);
    }
}
