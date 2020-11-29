package cn.xdevops.spring.controller;

import cn.xdevops.spring.vo.FruitVo;
import cn.xdevops.spring.service.FruitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/fruits")
public class FruitController {

    @Autowired
    private FruitService fruitService;

    @GetMapping
    public List<FruitVo> getAll() {
        return fruitService.fruitList();
    }

    @PostMapping
    public FruitVo createFruit(@RequestBody(required = false) FruitVo fruitVo) {
        return fruitService.save(fruitVo);
    }

    @GetMapping("/{id}")
    public FruitVo getFruit(@PathVariable("id") Long id) {
        return fruitService.findById(id);
    }

    @PutMapping("/{id}")
    public FruitVo updateFruit(@PathVariable("id") Long id, @RequestBody(required = false) FruitVo fruitVo) {
        return fruitService.updateFruit(id, fruitVo);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        fruitService.deleteById(id);
    }
}
