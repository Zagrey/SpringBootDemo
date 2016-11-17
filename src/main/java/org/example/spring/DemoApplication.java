package org.example.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@EnableTransactionManagement
@SpringBootApplication
@EnableCaching
public class DemoApplication {

//    @Autowired
//    RabbitTemplate rabbitTemplate;
//    @Value("${rabbit.input.queue}")
//    String queueName;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);

    }

    @Bean
    public CacheManager cacheManager() {
        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager("greetings");
        return cacheManager;
    }






//    @Bean
//    Queue queue() {
//        return new Queue(queueName, false);
//    }
//
//    @Bean
//    TopicExchange exchange() {
//        return new TopicExchange("spring-boot-exchange");
//    }
//
//    @Bean
//    Binding binding(Queue queue, TopicExchange exchange) {
//        return BindingBuilder.bind(queue).to(exchange).with(queueName);
//    }

//    @Override
//    public void run(String... args) throws Exception {
//        System.out.println("Waiting five seconds...");
//        Thread.sleep(5000);
//        System.out.println("Sending message...");
//        rabbitTemplate.convertAndSend(queueName, "Hello from RabbitMQ!");
//    }
}
