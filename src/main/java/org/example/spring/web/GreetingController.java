package org.example.spring.web;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.spring.model.Greeting;
import org.example.spring.service.GreetingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;


@RestController
public class GreetingController {
    @Autowired
    private GreetingService greetingService;
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Value("${rabbit.input.queue}")
    private String queueInputName;

    ObjectMapper mapper = new ObjectMapper();

    private static final Logger log = LoggerFactory.getLogger(GreetingController.class);

    @RequestMapping(
            value = "/api/greetings",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<Greeting>> getGreetings() {

        log.info("getGreeting: start");
        Collection<Greeting> greetings = greetingService.findAll();

        log.info("getGreeting: end");
        return new ResponseEntity<Collection<Greeting>>(greetings,
                HttpStatus.OK);
    }

    @RequestMapping(
            value = "/api/greetings/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Greeting> getGreeting(@PathVariable("id") Long id) {

        log.info("getGreeting: start");
        Greeting greeting = greetingService.findOne(id);
        if (greeting == null) {
            return new ResponseEntity<Greeting>(HttpStatus.NOT_FOUND);
        }
        log.info("getGreeting: end");

        return new ResponseEntity<Greeting>(greeting, HttpStatus.OK);
    }

    @RequestMapping(
            value = "/api/greetings",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Greeting> createGreeting(
            @RequestBody Greeting greeting) {

        Greeting savedGreeting = greetingService.create(greeting);

        try {
            rabbitTemplate.convertAndSend(queueInputName, mapper.writeValueAsString(savedGreeting));
        } catch (JsonProcessingException e) {
            throw new RuntimeException();
        }
        return new ResponseEntity<Greeting>(savedGreeting, HttpStatus.CREATED);
    }

    @RequestMapping(
            value = "/api/greetings/{id}",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Greeting> updateGreeting(
            @RequestBody Greeting greeting) {

        Greeting updatedGreeting = greetingService.update(greeting);
        if (updatedGreeting == null) {
            return new ResponseEntity<Greeting>(
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<Greeting>(updatedGreeting, HttpStatus.OK);
    }

    @RequestMapping(
            value = "/api/greetings/{id}",
            method = RequestMethod.DELETE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Greeting> deleteGreeting(@PathVariable("id") Long id,
                                                   @RequestBody Greeting greeting) {

        greetingService.delete(id);

        return new ResponseEntity<Greeting>(HttpStatus.NO_CONTENT);
    }
}
