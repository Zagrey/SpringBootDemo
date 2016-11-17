package org.example.spring;

import org.example.spring.model.Greeting;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
//@SpringBootTest(classes = DemoApplication.class)
@SpringApplicationConfiguration(classes = DemoApplication.class)
@WebIntegrationTest
public class DemoApplicationTestIT {

    RestTemplate restTemplate = new RestTemplate();

    @Test
    public void testGetAllGreetings() {

        ResponseEntity<List<Greeting>> responseEntity = restTemplate.exchange("http://localhost:8080/api/greetings",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Greeting>>() {
                });

        List<Greeting> actualList = responseEntity.getBody();
        assertThat(actualList.size(), is(12));

//        List<Long> ids = actualList.stream().map(Greeting::getId).collect(collectingAndThen(toList(), ImmutableList::copyOf));
        List<Long> ids = actualList.stream().map(Greeting::getId).collect(toList());

        assertThat(ids, containsInAnyOrder(2, 3));
    }


    @Test
    public void testMassiveAdd() {

        Map<String, String> vars = new HashMap<String, String>();

        for (int i = 0; i < 10000; i++){
            Greeting g = new Greeting();
            g.setText("user-" + i);
            String uri = "http://localhost:8080/api/greetings";
            Greeting response = restTemplate.postForObject(uri, g, Greeting.class, vars);
        }

        ResponseEntity<List<Greeting>> responseEntity = restTemplate.exchange("http://localhost:8080/api/greetings",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Greeting>>() {
                });

        List<Greeting> actualList = responseEntity.getBody();
        assertThat(actualList.size(), greaterThan(10));

    }
}
