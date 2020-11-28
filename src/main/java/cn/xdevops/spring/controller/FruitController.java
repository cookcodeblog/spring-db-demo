package cn.xdevops.spring.controller;

import cn.xdevops.spring.entiy.Fruit;
import cn.xdevops.spring.repository.FruitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/api/fruits")
public class FruitController {
    private FruitRepository fruitRepository;

    @Autowired
    public FruitController(FruitRepository fruitRepository) {
        this.fruitRepository = fruitRepository;
    }

    @GetMapping
    public List<Fruit> getAll() {
        return StreamSupport.stream(fruitRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    @PostMapping
    public Fruit createFruit(@RequestBody(required = false) Fruit fruit) {
        return fruitRepository.save(fruit);
    }

    @GetMapping("/{id}")
    public Fruit getFruit(@PathVariable("id") Long id) {
        return fruitRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Fruit updateFruit(@PathVariable("id") Long id, @RequestBody(required = false) Fruit fruit) {
        fruit.setId(id);
        return fruitRepository.save(fruit);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        fruitRepository.deleteById(id);
    }
}
