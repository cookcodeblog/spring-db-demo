package cn.xdevops.spring.controller;

import cn.xdevops.spring.config.MessageProperties;
import cn.xdevops.spring.vo.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class GreetingController {

    @Autowired
    private MessageProperties properties;

    @Value("${greeting.message}")
    private String greetingMessage;

    @GetMapping("/api/greeting")
    public Message greeting(@RequestParam(value = "name", defaultValue = "Apple") String name) {
        Objects.requireNonNull(properties.getMessage(), "Greeting message was not set in the properties");

        String message = String.format(properties.getMessage(), name);
        return new Message(message);
    }

    @GetMapping("/api/greeting2")
    public Message greeting2(@RequestParam(value = "name", defaultValue = "Apple") String name) {
        Objects.requireNonNull(greetingMessage, "Greeting message was not set in the properties");

        String message = String.format(greetingMessage, name);
        return new Message(message);
    }
}
