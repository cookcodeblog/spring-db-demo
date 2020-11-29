package cn.xdevops.spring.controller;

import cn.xdevops.spring.vo.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RefreshScope
public class GreetingRefreshScopeController {

    @Value("${greeting.message}")
    private String greetingMessage;

    @GetMapping("/api/greeting2")
    public Message greeting2(@RequestParam(value = "name", defaultValue = "Apple") String name) {
        Objects.requireNonNull(greetingMessage, "Greeting message was not set in the properties");

        String message = String.format(greetingMessage, name);
        return new Message(message);
    }
}
