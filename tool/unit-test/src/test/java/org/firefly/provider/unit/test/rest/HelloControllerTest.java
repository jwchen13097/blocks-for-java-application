package org.firefly.provider.unit.test.rest;

import org.firefly.provider.unit.test.domain.HelloService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class HelloControllerTest {
    @Mock
    private HelloService helloService;

    @InjectMocks
    private HelloController helloController;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders
                .standaloneSetup(helloController)
                .setControllerAdvice(WebExceptionHandler.class)
                .build();
    }

    @Test
    public void shouldReturnNormalMessageWhenRequestReceived() throws Exception {
        when(helloService.greet("cjw")).thenReturn("Hello, cjw!");

        mockMvc.perform(get("/hello/{name}", "cjw").header("Content-Type", MediaType.TEXT_PLAIN_VALUE))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(content().string("Hello, cjw!"));
    }

    @Test
    public void shouldThrowExceptionWhenExceptionHappens() throws Exception {
        when(helloService.greet("cjw")).thenThrow(new NullPointerException("Name is empty"));

        mockMvc.perform(get("/hello/{name}", "cjw").header("Content-Type", MediaType.TEXT_PLAIN_VALUE))
                .andExpect(status().is(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .andExpect(content().string("Name is empty"));
    }

    @Test
    public void shouldSayHelloToPeopleWhenGivenTheName() {
        String name = "Louis";
        when(helloService.greet(name)).thenReturn("Hello, " + name);

        String greetingWord = helloController.hello(name);

        assertThat(greetingWord, is("Hello, Louis"));
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowExceptionWhenGreetFail() {
        String name = "Louis";
        when(helloService.greet(name)).thenThrow(new RuntimeException());

        helloController.hello(name);
    }
}