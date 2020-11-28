package cn.xdevops.spring.controller;

import cn.xdevops.spring.dto.FruitDto;
import cn.xdevops.spring.service.FruitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/fruits")
public class FruitController {

    @Autowired
    private final FruitService fruitService;

    public FruitController(FruitService fruitService) {
        this.fruitService = fruitService;
    }

    @GetMapping
    public List<FruitDto> getAll() {
        return fruitService.fruitList();
    }

    @PostMapping
    public FruitDto createFruit(@RequestBody(required = false) FruitDto fruitDto) {
        return fruitService.save(fruitDto);
    }

    @GetMapping("/{id}")
    public FruitDto getFruit(@PathVariable("id") Long id) {
        return fruitService.findById(id);
    }

    @PutMapping("/{id}")
    public FruitDto updateFruit(@PathVariable("id") Long id, @RequestBody(required = false) FruitDto fruitDto) {
        return fruitService.updateFruit(id, fruitDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        fruitService.deleteById(id);
    }
}
