package cn.xdevops.spring.controller;

import cn.xdevops.spring.entiy.Fruit;
import cn.xdevops.spring.service.IFruitService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fruits")
public class FruitController {

     private final IFruitService iFruitService;

    public FruitController(IFruitService iFruitService) {
        this.iFruitService = iFruitService;
    }

    @GetMapping
    public List<Fruit> getAll() {
        return iFruitService.fruitList();
    }

    @PostMapping
    public Fruit createFruit(@RequestBody(required = false) Fruit fruit) {
        return iFruitService.save(fruit);
    }

    @GetMapping("/{id}")
    public Fruit getFruit(@PathVariable("id") Long id) {
        return iFruitService.findById(id);
    }

    @PutMapping("/{id}")
    public Fruit updateFruit(@PathVariable("id") Long id, @RequestBody(required = false) Fruit fruit) {
        return iFruitService.updateFruit(id, fruit);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {

    }
}
