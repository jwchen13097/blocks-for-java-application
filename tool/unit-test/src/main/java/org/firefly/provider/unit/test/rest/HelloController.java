package org.firefly.provider.unit.test.rest;

import org.firefly.provider.unit.test.domain.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloController {
    @Autowired
    private HelloService helloService;

    @GetMapping("/{name}")
    public String hello(@PathVariable String name) {
        return helloService.greet(name);
    }
}
