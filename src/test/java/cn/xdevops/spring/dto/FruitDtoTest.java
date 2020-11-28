package cn.xdevops.spring.dto;

import cn.xdevops.spring.entiy.Fruit;
import org.junit.Before;
import org.junit.Test;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

/**
 * Model Mapper Test
 * <p>
 * https://github.com/modelmapper/modelmapper
 * https://github.com/jmnarloch/modelmapper-spring-boot-starter
 * http://modelmapper.org/getting-started/
 */
public class FruitDtoTest {

    private ModelMapper modelMapper;

    @Before
    public void setUp() {
        modelMapper = new ModelMapper();
    }

    @Test
    public void test_convert_dto_to_entity() {
        FruitDto dto = new FruitDto(1L, "Apple");
        Fruit entity = modelMapper.map(dto, Fruit.class);
        assertThat(entity).extracting(Fruit::getId, Fruit::getName).containsExactly(dto.getId(), dto.getName());
    }

    @Test
    public void test_convert_entity_to_dto() {
        Fruit entity = new Fruit(1L, "Apple");
        FruitDto dto = modelMapper.map(entity, FruitDto.class);
        assertThat(dto).extracting(FruitDto::getId, FruitDto::getName).containsExactly(entity.getId(), entity.getName());
    }

    @Test
    public void test_convert_entity_list_to_dto_list() {
        List<Fruit> entityList = Arrays.asList(new Fruit(1L, "Apple"),
                new Fruit(2L, "Banana"),
                new Fruit(3L, "Cherry"));

        List<FruitDto> dtoList = entityList.stream().map(x -> modelMapper.map(x, FruitDto.class)).collect(Collectors.toList());
        assertThat(dtoList).extracting(FruitDto::getId, FruitDto::getName)
                .containsExactly(tuple(1L, "Apple"),
                        tuple(2L, "Banana"),
                        tuple(3L, "Cherry"));
    }

}