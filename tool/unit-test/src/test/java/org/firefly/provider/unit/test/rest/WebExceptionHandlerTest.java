package org.firefly.provider.unit.test.rest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class WebExceptionHandlerTest {
    @InjectMocks
    private WebExceptionHandler webExceptionHandler;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldReturnEntityWhenExceptionHappens() {
        ResponseEntity<String> responseEntity =
                webExceptionHandler.handleAnyException(new Exception("Unknown Exception"));

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.INTERNAL_SERVER_ERROR));
        assertThat(responseEntity.getBody(), is("Unknown Exception"));
    }
}