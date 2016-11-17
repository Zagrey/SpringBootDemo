package org.example.spring.service;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

/**
 * Created by zagrey on 10/24/2016.
 */
public class GreetingServiceBeanJpaTest {

    @Autowired
    GreetingService service;

    @Before
    public void setUp() throws Exception {


    }

    @Test
    public void findAll() throws Exception {

    }

}