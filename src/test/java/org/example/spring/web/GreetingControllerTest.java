package org.example.spring.web;

import org.example.spring.service.GreetingService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GreetingControllerTest {
    @Mock
    GreetingService greetingService;
    @InjectMocks
    GreetingController gut;

    @Test
    public void getGreetings() throws Exception {

        when(greetingService.findAll()).thenReturn(new ArrayList<>());
        gut.getGreetings();
        verify(greetingService).findAll();
    }

}